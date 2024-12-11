from datetime import datetime
from django.shortcuts import render, redirect
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from app.models import *
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth import login as django_login
from django.contrib.auth import login as django_login
from .serializers import ProductSerializer, LoginSerializer, AssignSerializer, AssetSerializer, UserSerializer, BarcodeUpdateSerializer, RequestAssetSerializer
from django.utils import timezone
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.serializers.json import DjangoJSONEncoder
from geopy.geocoders import Nominatim

@api_view(['POST'])
def login_view(request):
    # print(f"Received data: {request.data}")  # Print raw request data
    
    # Check if request contains data
    # if not request.data:
    #     print("No data received.")
    
    # Deserialize the incoming data
    serializer = LoginSerializer(data=request.data)
    print(f"Serializer is valid: {serializer.is_valid()}")

    if serializer.is_valid():
        username = serializer.validated_data['username']
        password = serializer.validated_data['password']

        # Debugging step: print received username and password
        # print(f"Username: {username}")
        # print(f"Password: {password}")

        try:
            # Check if user exists
            user = UserDetails.objects.get(username=username)
            
            # Debugging step: print the stored password
            # print(f"Stored password: {user.password}")
            
            if user.password == password:
                user.last_login = timezone.now()
                user.save()  # Save the updated last_login time
                
                # request.session['user_id'] = user.user_id
                request.session['username'] = user.username
                request.session['role'] = user.role.role  # Example custom field
                request.session['full_name'] = user.first_name

                # Log the user in
                django_login(request, user)

                return Response({
                    'message': 'Login successful',
                    'session_id': request.session.session_key,
                    'username': user.username,
                    'role': user.role.role,
                }, status=status.HTTP_200_OK)
            else:
                return Response({'error': 'Invalid password'}, status=status.HTTP_400_BAD_REQUEST)
        except UserDetails.DoesNotExist:
            return Response({'error': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
    
    # If the serializer is not valid, return the errors
    print(f"Serializer errors: {serializer.errors}")
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def get_categories(request):
    categories = AssetCategory.objects.all()
    data = []
    for category in categories:
        subcategories = AssetSubCategory.objects.filter(category=category)
        data.append({
            "category_id": category.category_id,
            "category_name": category.category_name,
            "subcategories": [
                {
                    "sub_category_id": sub.sub_category_id,
                    "sub_category_name": sub.sub_category_name
                }
                for sub in subcategories
            ]
        })
    return Response(data, status=200)

@api_view(['GET'])
def get_condition_choices(request):
    conditions = [condition[0] for condition in CONDITION_CHOICES]
    return Response(conditions)

@api_view(['POST'])
def add_product(request):
    serializer = ProductSerializer(data=request.data)
    print(request.data)

    if not serializer.is_valid():
        print("Validation Errors:", serializer.errors)
        return Response(serializer.errors, status=400)

    else: 
        # Extract validated data from the request
        assetName = serializer.validated_data['asset_name']
        assetValue = serializer.validated_data['asset_value']
        barcode = serializer.validated_data['barcode']
        category = serializer.validated_data['category']
        condition = serializer.validated_data['condition']
        location = serializer.validated_data['location']  # Format: 'longitude,latitude'
        purchaseDate = serializer.validated_data['purchase_date']
        subcategory = serializer.validated_data['subcategory']  # Assuming this is passed as subcategory_id
        print(location)
        # Convert location coordinates into a readable name
        try:
            latitude, longitude = map(float, location.split(','))
            geolocator = Nominatim(user_agent="asset_management")
            location_name = geolocator.reverse((latitude, longitude)).raw['address']
            print(location_name)
            road_name = location_name.get('road')
            city_name = location_name.get('state_district')
            district_name = location_name.get('city_district')
            specific_area_name = road_name + ', ' + city_name + ', ' + district_name
            print(specific_area_name)
        except Exception as e:
            return Response({"error": f"Failed to resolve location name: {str(e)}"}, status=400)

        # Get the AssetSubCategory instance based on the provided subcategory_id
        try:
            subcategory = AssetSubCategory.objects.get(sub_category_name=subcategory)
        except AssetSubCategory.DoesNotExist:
            return Response({"error": "Subcategory not found"}, status=404)

        # Create a new Asset instance with the related subcategory
        asset = Asset.objects.create(
            asset_name=assetName, 
            barcode=barcode, 
            asset_category=subcategory,  # This is the ForeignKey relation
            purchase_date=purchaseDate, 
            asset_value=assetValue, 
            condition=condition, 
            location=specific_area_name  # Save the specific area name
        )

        # Uncomment if maintenance needs to be created
        # Maintenance.objects.create(
        #     asset=asset,
        #     last_maintenance_date=asset.purchase_date,
        #     next_maintenance_date=asset.purchase_date + timedelta(days=180),  # Example: 180 days after purchase
        #     maintenance_cost='N/A',  # Set a default cost or pass it from the request
        # )

        return Response({"message": "Product added successfully!", "location_name": specific_area_name}, status=201)

    return Response(serializer.errors, status=400)


@api_view(['GET'])
def user_list_view(request):
    users = UserDetails.objects.all()  # Get all users
    serializer = UserSerializer(users, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)

@api_view(['GET'])
def AssetListView(request):
    filter_type = request.query_params.get('filter')

    if filter_type == 'available':
        assets = Asset.objects.filter(asset_status='available')
    elif filter_type == 'in-use':
        assets = Asset.objects.filter(asset_status='in-use')
    elif filter_type == 'in-maintenance':
        assets = Asset.objects.filter(asset_status='in-maintenance')
    elif filter_type == 'expired':
        assets = Asset.objects.filter(asset_status='expired')
    elif filter_type == 'barcode-remaining':
        assets = Asset.objects.filter(barcode__isnull=True)
    else:
        assets = Asset.objects.all()

    serializer = AssetSerializer(assets, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)


def index(request):
    userCount = UserDetails.objects.count()
    assetCount = Asset.objects.count()
    availableAsset = Asset.objects.filter(assign_to__isnull=True).count()
    inUseAsset = Asset.objects.filter(assign_to__isnull=False).count()
    return render(request,'index.html', {
                'userCount': userCount, 
                'assetCount': assetCount, 
                'availableAsset': availableAsset, 
                'inUseAsset': inUseAsset
    })
    
@api_view(['PUT'])
def update_barcode(request, asset_id):
    try:
        asset= Asset.objects.get(asset_id=asset_id)
    except Asset.DoesNotExist:
        return Response({"error": "Asset not found"}, status=status.HTTP_404_NOT_FOUND)

    serializer = BarcodeUpdateSerializer(data=request.data)
    print(request.data)
    if serializer.is_valid():
        asset.barcode = serializer.validated_data['barcode']
        asset.save()
        return Response({"message": "Barcode updated successfully"}, status=status.HTTP_200_OK)
    print(serializer.errors)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['GET'])
def get_requests(request):
    try:
        requests = RequestAsset.objects.all()
        serializer = RequestAssetSerializer(requests, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    except Exception as e:
        return Response({"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
@api_view(['GET'])
def get_totals(request):
    try:
        total_products = Asset.objects.count()
        total_users = UserDetails.objects.count()
        
        data = {
            "total_products": total_products,
            "total_users": total_users,
        }
        return Response(data, status=status.HTTP_200_OK)
    except Exception as e:
        return Response({"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

@api_view(['GET'])
def get_product_by_barcode(request, barcode):
    try:
        # Try to find the product with the given barcode
        product = Asset.objects.get(barcode=barcode)

        serializer = AssetSerializer(product, many=False)

        print(serializer.data)  # Debugging log to verify data
        return Response(serializer.data, status=status.HTTP_200_OK)
    except Asset.DoesNotExist:
        # If no product is found with the given barcode
        return JsonResponse({'status': 'error', 'message': 'Product not found with this barcode.'})
     
def signin(request):
    if "email" in request.session:
        return redirect('index')  # Redirect to the appropriate page for logged-in users
    
    if request.method == 'POST':
        email = request.POST['email']
        password = request.POST['password']
        try:
            # Fetch user by email
            uid = UserDetails.objects.get(email=email)
            print(email)
            # Check password match
            if uid.password == password:
                # Set session only if the password is correct
                request.session['email'] = uid.email
                return redirect("index")  # Redirect to the dashboard or homepage
            else:
                # Invalid password
                con = {"e_msg": "Invalid password"}
                return render(request, "signin.html", con)
        except UserDetails.DoesNotExist:
            # User does not exist
            con = {'e_msg': 'User does not exist'}
            return render(request, 'signin.html', con)

    return render(request, 'signin.html')

def logout(request):
    if 'email' in request.session:
        del request.session['email']
        return redirect('signin')
    else:
        return redirect('signin')
    
def forgetpassword(request):
    return render(request,'forgetpassword.html')

def productlist(request):
    return render(request,'productlist.html')

def editproduct(request):
    return render(request,'editproduct.html')

def productdetails(request):
    return render(request,'productdetails.html')
def addproduct(request):
    return render(request,'addproduct.html')

def categorylist(request):
    return render(request,'categorylist.html')

def addcategory(request):
    return render(request,'addcategory.html')
def subcategorylist(request):
    return render(request,'subcategorylist.html')
def addsubcategory(request):
    return render(request,'subaddcategory.html')
def editcategory(request):
    return render(request,'editcategory.html')
def editsubcategory(request):
    return render(request,'editsubcategory.html')

def importproduct(request):
    return render(request,'importproduct.html')

def barcode(request):
    return render(request,'barcode.html')

# def importproduct(request):
#     return render(request,'importproduct.html')
#     return render(request,'barcode.html')

def issuedproducts(request):
    return render(request,'issuedproducts.html')

def editissuedproducts(request):
    return render(request,'editissuedproducts.html')

def addissuedproducts(request):
    return render(request,'addissuedproducts.html')

def maintenanceproducts(request):
    maintenance_id = Maintenance.objects.all()
    con = {'maintenance_id':maintenance_id}
    return render(request,'maintenanceproducts.html',con)

def editmaintenanceproducts(request,id):
    maintenance_id = Maintenance.objects.get(maintenance_id=id)
    if request.method == 'POST':
        asset_name = request.POST['asset_name']
        barcode = request.POST['barcode']
        last_maintenance_date = request.POST['last_maintenance_date']
        next_maintenance_date = request.POST['next_maintenance_date']
        return_date = request.POST['return_date']
        maintenance_cost = request.POST['maintenance_cost']
        # print(last_maintenance_date)
     
        try:
            asset = Asset.objects.get(barcode=barcode)
            maintenance_id.asset = asset
            maintenance_id.last_maintenance_date = last_maintenance_date
            maintenance_id.next_maintenance_date = next_maintenance_date
            maintenance_id.return_date=return_date
            maintenance_id.maintenance_cost=maintenance_cost
            maintenance_id.save()

            return redirect('maintenanceproducts')
        
        except Asset.DoesNotExist:
            return render(request, 'editmaintenanceproducts.html', {
                'i': maintenance_id,
                'error': "Asset with the given name and barcode does not exist."
            })
        
    return render(request,'editmaintenanceproducts.html',{'i':maintenance_id})

def addmaintenanceproducts(request):
    if request.method == 'POST':
        asset_name = request.POST['asset_name']
        barcode = request.POST['barcode']
        last_maintenance_date = request.POST['last_maintenance_date']
        next_maintenance_date = request.POST['next_maintenance_date']
        return_date = request.POST['return_date']
        maintenance_cost = request.POST['maintenance_cost']
        Maintenance.objects.create(asset_name=asset_name,barcode=barcode,last_maintenance_date=last_maintenance_date,
                                   next_maintenance_date=next_maintenance_date,return_date=return_date,maintenance_cost=maintenance_cost)
        return redirect("maintenanceproducts")
    return render(request,'addmaintenanceproducts.html')


def expiredproducts(request):
    expired_id = ExpiredProduct.objects.all()
    con={'expired_id':expired_id}
    return render(request,'expiredproducts.html',con)

def editexpiredproducts(request,id):
    expired_id=ExpiredProduct.objects.get(expired_id=id)
    if request.method == 'POST':
        asset_name = request.POST['asset_name']
        barcode = request.POST['barcode']
        expiration_date = request.POST['expiration_date']
        reason = request.POST['reason']

        try:
            asset = Asset.objects.get(barcode=barcode)
            expired_id.asset = asset
            expired_id.expiration_date = expiration_date
            expired_id.reason=reason
            expired_id.save()
            return redirect('expiredproducts')
        
        except Asset.DoesNotExist:
            return render(request, 'editexpiredproducts.html', {
                'i': expired_id,
                'error': "Asset with the given name and barcode does not exist."
            })

    return render(request,'editexpiredproducts.html',{'i': expired_id})

def addexpiredproducts(request):
    return render(request,'addexpiredproducts.html')

def returnproducts(request):
    return render(request,'returnproducts.html')

def editreturnproducts(request):
    return render(request,'editreturnproducts.html')

def addreturnproducts(request):
    return render(request,'addreturnproducts.html')

def aa(request):
    return render(request,'aa.html')

def newuser(request):
    if request.method == 'POST':
        # Get data from the POST request
        username = request.POST.get('username')
        role_name = request.POST.get('role_name')
        email = request.POST.get('email')
        password = request.POST.get('password')
        station_name = request.POST.get('station_name')
        mobile = request.POST.get('mobile')
        print(username)
        print(station_name)
        print(email)
        # Get related Role and Station objects
        roleGet = role.objects.get(role=role_name)
        station = stationDetails.objects.get(station_name=station_name)

        UserDetails.objects.create(
            username=username,
            role=roleGet,
            email=email,
            password=password,
            station=station,
            contact_number=mobile,
            full_name="amaan shaikh"
        )
        
        return redirect('newuser')

    roles = role.objects.all()
    station = stationDetails.objects.all()
    
    return render(request,'newuser.html', {'roles': roles, 'station': station})

def userlists(request):
    users = UserDetails.objects.all()
    return render(request,'userlists.html', {'users': users})

def edituser(request):
    return render(request,'edituser.html')

def expenseList(request):
    return render(request,'expenselist.html')

def createExpense(request):
    return render(request,'createexpense.html')

def editExpense(request):
    return render(request,'editexpense.html')

def expenseCategory(request):
    return render(request,'expenseCategory.html')

def quotationList(request):
    return render(request,'quotationList.html')

def addquotation(request):
    return render(request,'addquotation.html')

def stationlist(request):
    station_id = stationDetails.objects.all()
    con = {"station_id":station_id}
    return render(request,'stationlist.html',con)

def newstation(request):
    if request.POST:
        station_name = request.POST['station_name']
        station_code = request.POST['station_code']
        station_address = request.POST['station_address']
        stationDetails.objects.create(station_name=station_name,station_code=station_code,station_address=station_address)
        return redirect('stationlist')
    return render(request,'newstation.html')

def editstation(request,id):
    station_id = stationDetails.objects.get(station_id=id)
    if request.POST:
        station_name = request.POST['station_name']
        station_code = request.POST['station_code']
        station_address = request.POST['station_address']

        station_id.station_name = station_name
        station_id.station_code = station_code
        station_id.station_address = station_address

        station_id.save()

        return redirect('stationlist')
    return render(request,'editstation.html',{'i': station_id})

def deletestation(request,id):
    station_id = stationDetails.objects.get(station_id=id)
    station_id.delete()

    return redirect('stationlist')




def editQuotation(request):
    return render(request,'editquotation.html')

def editExpense(request):
    return render(request,'editExpense.html')

def profile(request):
    return render(request,'profile.html')

def generalSettings(request):
    return render(request,'editexpense.html')



def signup(request):
    return render(request,'signup.html')



from django.core.exceptions import ObjectDoesNotExist

@api_view(['POST'])
def assign_product(request):
    # Deserialize the incoming data
    serializer = AssignSerializer(data=request.data)
    print(request.data)
    if serializer.is_valid():
        # Extract the necessary fields
        barcode = serializer.validated_data['barcode']
        returnDate = serializer.validated_data['return_date']
        user = serializer.validated_data['username']
        location = serializer.validated_data['location']
        print(user)
        
        try:
            # Fetch the asset from the database
            asset = Asset.objects.get(barcode=barcode)
            
            if asset.assign_to is None:    
                user = UserDetails.objects.get(username=user)
                # Create a new Allocation object and save it to the database
                allocation = Allocation.objects.create(
                    asset=asset,
                    user=user,
                    expected_return_date=returnDate,
                    assign_location=location
                )
                
                asset.assign_to = user
                asset.asset_status = 'in-use'
                asset.save()
                
                return Response({"message": "Product assigned successfully!", "allocation_id": allocation.allocation_id}, status=201)
            else:
                return Response({"message": "Product is already assigned!"}, status=400)
        except ObjectDoesNotExist:
            return Response({"message": "Product not found with barcode!"}, status=404)
    else:
        print("Validation Errors:", serializer.errors)
        return Response(serializer.errors, status=400)



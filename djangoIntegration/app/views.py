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
from .serializers import ProductSerializer, LoginSerializer, AssignSerializer, AssetSerializer, UserSerializer
from django.utils import timezone
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.core.serializers.json import DjangoJSONEncoder


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
            user = User.objects.get(username=username)
            
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
        except User.DoesNotExist:
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
        location = serializer.validated_data['location']
        purchaseDate = serializer.validated_data['purchase_date']
        subcategory = serializer.validated_data['subcategory']  # Assuming this is passed as subcategory_id
        
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
            location=location
        )
        
        # Maintenance.objects.create(
        #     asset=asset,
        #     last_maintenance_date=asset.purchase_date,
        #     next_maintenance_date=asset.purchase_date + timedelta(days=180),  # Example: 180 days after purchase
        #     maintenance_cost='N/A',  # Set a default cost or pass it from the request
        # )
        
        return Response({"message": "Product added successfully!"}, status=201)
    
    return Response(serializer.errors, status=400)

@api_view(['GET'])
def user_list_view(request):
    users = User.objects.all()  # Get all users
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
    userCount = User.objects.count()
    assetCount = Asset.objects.count()
    availableAsset = Asset.objects.filter(assign_to__isnull=True).count()
    inUseAsset = Asset.objects.filter(assign_to__isnull=False).count()
    return render(request,'index.html', {
                'userCount': userCount, 
                'assetCount': assetCount, 
                'availableAsset': availableAsset, 
                'inUseAsset': inUseAsset
    })
    
@api_view(['GET'])
def get_totals(request):
    try:
        total_products = Asset.objects.count()
        total_users = User.objects.count()
        
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
    
def productlist(request):
    return render(request,'productlist.html')

def editproduct(request):
    return render(request,'editproduct.html')

def productdetails(request):
    return render(request,'productdetails.html')
def addproduct(request):
    return render(request,'addproduct.html')

def categorylist(request):
    categories=AssetCategory.objects.all()
    return render(request,'categorylist.html',{'categories':categories})

def addcategory(request):
    if request.method=="POST":
        category=request.POST.get('category')
        AssetCategory.objects.create(category_name=category)
        print(category)
        return redirect("categorylist")
        
        
    return render(request,'addcategory.html')
def subcategorylist(request):
    subcategories=AssetSubCategory.objects.all()
    return render(request,'subcategorylist.html',{'subcategories':subcategories})

def addsubcategory(request):
    if request.method == 'POST':
        # Get data from the POST request
        subcategory=request.POST.get('subcategory')
        categories = request.POST.get('category_name')
        print(subcategory)
        # Get related Role and Station objects
        categoryGet = AssetCategory.objects.get(category_name=categories)
        AssetSubCategory.objects.create(
            category=categoryGet,
            sub_category_name=subcategory,
        )
        
        return redirect('subcategorylist')

    categories = AssetCategory.objects.all()
    
    return render(request,'subaddcategory.html',{'categories':categories})

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
    return render(request,'maintenanceproducts.html')

def editmaintenanceproducts(request):
    return render(request,'editmaintenanceproducts.html')

def addmaintenanceproducts(request):
    return render(request,'addmaintenanceproducts.html')


def expiredproducts(request):
    return render(request,'expiredproducts.html')

def editexpiredproducts(request):
    return render(request,'editexpiredproducts.html')

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
        firstname = request.POST.get('firstname')
        lastname = request.POST.get('lastname')
        username = request.POST.get('username')
        role_name = request.POST.get('role_name')
        email = request.POST.get('email')
        password = request.POST.get('password')
        station_name = request.POST.get('station_name')
        mobile = request.POST.get('mobile')
        is_active = request.POST.get('is_active') == 'on'  # Checkbox returns 'on' if checked

        print(username)
        print(station_name)
        print(email)
        # Get related Role and Station objects
        roleGet = role.objects.get(role=role_name)
        station = stationDetails.objects.get(station_name=station_name)

        User.objects.create(
            first_name=firstname,
            last_name=lastname,
            username=username,
            role=roleGet,
            email=email,
            password=password,
            station=station,
            contact_number=mobile,
            is_active=is_active,

        )
        
        return redirect('newuser')

    roles = role.objects.all()
    station = stationDetails.objects.all()
    
    return render(request,'newuser.html', {'roles': roles, 'station': station})

def userlists(request):
    users = User.objects.all()
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

def signin(request):
    return render(request,'signin.html')

def signup(request):
    return render(request,'signup.html')

def forgetpassword(request):
    return render(request,'forgetpassword.html')

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
                user = User.objects.get(username=user)
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



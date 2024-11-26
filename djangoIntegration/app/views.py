from django.shortcuts import render
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
                request.session['full_name'] = user.full_name

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

@api_view(['POST'])
def add_product(request):
    serializer = ProductSerializer(data=request.data)
    print(request.data)
    if serializer.is_valid():
        barcode = serializer.validated_data['barcode']
        assetType = serializer.validated_data['asset_type']
        assetName = serializer.validated_data['asset_name']
        purchaseDate = serializer.validated_data['purchase_date']
        assetValue = serializer.validated_data['asset_value']
        condition = serializer.validated_data['condition']
        location = serializer.validated_data['Location']
        
        Asset.objects.create(asset_name=assetName, barcode=barcode, asset_type=assetType, purchase_date=purchaseDate, asset_value=assetValue, condition=condition, location=location)
        
        return Response({"message": "Product added successfully!"}, status=201)
    return Response(serializer.errors, status=400)

@api_view(['GET'])
def user_list_view(request):
    users = User.objects.all()  # Get all users
    serializer = UserSerializer(users, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)



def index(request):
    print("Index view called")
    try:
        userCount = User.objects.count()
        productCount = Asset.objects.count()
    except Exception as e:
        print(f"Error: {e}")
    return render(request, 'index.html', {'userCount': userCount, 'productCount': productCount})

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
def editcategory(request):
    return render(request,'editcategory.html')

# def importproduct(request):
#     return render(request,'importproduct.html')
# def barcode(request):
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
    return render(request,'newuser.html')
def userlists(request):
    return render(request,'userlists.html')
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
    return render(request,'stationlist.html')

def newstation(request):
    return render(request,'newstation.html')

def editstation(request):
    return render(request,'editstation.html')

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
    if serializer.is_valid():
        # Extract the necessary fields
        barcode = serializer.validated_data['barcode']
        returnDate = serializer.validated_data['return_date']
        user = serializer.validated_data['username']
        
        try:
            # Fetch the asset from the database
            asset = Asset.objects.get(barcode=barcode)
            
            if asset.assign_to is None:    
                # Create a new Allocation object and save it to the database
                allocation = Allocation.objects.create(
                    asset_barcode=barcode,
                    user=user,
                    return_date=returnDate,
                    assign_location="NULL"
                )
                
                asset.assign_to = user
                asset.save()
                
                return Response({"message": "Product assigned successfully!", "allocation_id": allocation.allocation_id}, status=201)
            else:
                return Response({"message": "Product is already assigned!"}, status=400)
        except ObjectDoesNotExist:
            return Response({"message": "Product not found with barcode!"}, status=404)
    else:
        return Response(serializer.errors, status=400)

@api_view(['GET'])
def AssetListView(request):
    assets = Asset.objects.all()
    serializer = AssetSerializer(assets, many=True)
    return Response(serializer.data, status=status.HTTP_200_OK)

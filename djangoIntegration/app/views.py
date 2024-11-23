from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import *
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from .serializers import ProductSerializer, LoginSerializer


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
                
                return Response({'message': 'Login successful'}, status=status.HTTP_200_OK)
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
    if serializer.is_valid():
        barcode = serializer.validated_data['barcode']
        pname = serializer.validated_data['product_name']
        pprice = serializer.validated_data['product_price']
        
        Product.objects.create(barcode=barcode, product_name=pname, product_price=pprice)
        
        return Response({"message": "Product added successfully!"}, status=201)
    return Response(serializer.errors, status=400)



def index(request):
    return render(request,'index.html')

def productlist(request):
    return render(request,'productlist.html')
def editproduct(request):
    return render(request,'editproduct.html')
def addproduct(request):
    return render(request,'addproduct.html')

def categorylist(request):
    return render(request,'categorylist.html')
def addcategory(request):
    return render(request,'addcategory.html')
def editcategory(request):
    return render(request,'editcategory.html')

def importproduct(request):
    return render(request,'importproduct.html')
def barcode(request):
    return render(request,'barcode.html')

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
def index(request):
    return render(request,'index.html')


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

def countriesList(request):
    return render(request,'countriesList.html')

def newCountry(request):
    return render(request,'newCountry.html')

def editCountry(request):
    return render(request,'editCountry.html')

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

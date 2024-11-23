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
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
        assetType = serializer.validated_data['asset_type']
        assetName = serializer.validated_data['asset_name']
        purchaseDate = serializer.validated_data['purchase_date']
        assetValue = serializer.validated_data['asset_value']
        condition = serializer.validated_data['condition']
        
        Asset.objects.create(asset_name=assetName, barcode=barcode, asset_type=assetType, purchase_date=purchaseDate, asset_value=assetValue, condition=condition)
        
        return Response({"message": "Product added successfully!"}, status=201)
    return Response(serializer.errors, status=400)
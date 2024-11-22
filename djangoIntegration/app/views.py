from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import *
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth import login as django_login
from django.contrib.auth import login as django_login
from .serializers import ProductSerializer, LoginSerializer, AssignSerializer, AssetSerializer
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
    if serializer.is_valid():
        barcode = serializer.validated_data['barcode']
        assetType = serializer.validated_data['asset_type']
        assetName = serializer.validated_data['asset_name']
        purchaseDate = serializer.validated_data['purchase_date']
        assetValue = serializer.validated_data['asset_value']
        condition = serializer.validated_data['condition']
        # location = serializer.validated_data['location']
        
        Asset.objects.create(asset_name=assetName, barcode=barcode, asset_type=assetType, purchase_date=purchaseDate, asset_value=assetValue, condition=condition, location="NULL")
        
        return Response({"message": "Product added successfully!"}, status=201)

from django.core.exceptions import ObjectDoesNotExist

@api_view(['POST'])
def assign_product(request):
    # Deserialize the incoming data
    serializer = AssignSerializer(data=request.data)
    if serializer.is_valid():
        barcode = serializer.validated_data['barcode']
        return_date = serializer.validated_data['return_date']
        username = serializer.validated_data['username']
        
        try:
            # Fetch the asset using the barcode
            asset = Asset.objects.get(barcode=barcode)
            
            # Fetch the user from the database
            try:
                user = User.objects.get(username=username)
            except User.DoesNotExist:
                return Response({"message": "User not found!"}, status=404)

            # Check if the asset is already assigned
            if asset.assign_to is None:
                # Create the allocation object
                allocation = Allocation.objects.create(
                    asset_barcode=barcode,
                    user=user,
                    return_date=return_date,
                    assign_location="NULL"  # Adjust this as per your logic
                )
                
                # Update the `assign_to` field of the asset
                asset.assign_to = user
                asset.save()
                
                return Response({"message": "Product assigned successfully!", "allocation_id": allocation.allocation_id}, status=201)
            else:
                return Response({"message": "Product is already assigned!"}, status=400)
        
        except Asset.DoesNotExist:
            return Response({"message": "Product not found with barcode!"}, status=404)
    else:
        return Response(serializer.errors, status=400)
    
@api_view(['GET'])
def AssetList(request):
    # Fetch all assets from the database
    assets = Asset.objects.all()

    # Serialize the assets
    serializer = AssetSerializer(assets, many=True)

    # Return the serialized data as a response
    return Response(serializer.data, status=200)

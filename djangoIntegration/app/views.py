from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import signup
from .serializers import LoginSerializer
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from .models import signup
from .serializers import LoginSerializer

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
            user = signup.objects.get(username=username)
            
            # Debugging step: print the stored password
            print(f"Stored password: {user.password}")
            
            if user.password == password:
                return Response({'message': 'Login successful'}, status=status.HTTP_200_OK)
            else:
                return Response({'error': 'Invalid password'}, status=status.HTTP_400_BAD_REQUEST)
        except signup.DoesNotExist:
            return Response({'error': 'User not found'}, status=status.HTTP_404_NOT_FOUND)
    
    # If the serializer is not valid, return the errors
    print(f"Serializer errors: {serializer.errors}")
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
from rest_framework import serializers
from django.contrib.auth.models import *
from .models import *

class LoginSerializer(serializers.Serializer):
    username = serializers.CharField(max_length=255)
    password = serializers.CharField(max_length=255)

class ProductSerializer(serializers.Serializer):
    barcode = serializers.CharField(max_length=255)
    asset_name = serializers.CharField(max_length=255)
    category = serializers.CharField(max_length=255)
    subcategory = serializers.CharField(max_length=255)
    purchase_date = serializers.DateField()
    asset_value = serializers.CharField(max_length=255)
    condition = serializers.CharField(max_length=255)
    location = serializers.CharField(max_length=255)

class AssignSerializer(serializers.Serializer):
    barcode = serializers.CharField(max_length=255)
    return_date = serializers.DateField()
    username = serializers.CharField(max_length=100)
    location = serializers.CharField(max_length=100)
    
class AssetSerializer(serializers.ModelSerializer):
    asset_category = serializers.CharField(source='asset_category.category.category_name', read_only=True)  
    asset_sub_category = serializers.CharField(source='asset_category.sub_category_name', read_only=True)   
    assign_to = serializers.CharField(source='assign_to.username', read_only=True)   
    class Meta:
        model = Asset
        fields = ['asset_id', 'asset_name', 'barcode', 'purchase_date', 'asset_value', 'condition', 'location', 'asset_category', 'asset_sub_category', 'assign_to']  # or specify individual fields
        
class UserSerializer(serializers.ModelSerializer):
    role = serializers.CharField(source='role.role', read_only=True)  # Assuming 'name' is the field in the Role model
    station = serializers.CharField(source='station.station_name', read_only=True)  # Assuming 'station_name' is the field in the StationDetails model

    class Meta:
        model = User
        fields = ['first_name', 'last_name', 'role', 'station', 'contact_number']
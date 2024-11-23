from rest_framework import serializers
from django.contrib.auth.models import *
from .models import *

class LoginSerializer(serializers.Serializer):
    username = serializers.CharField(max_length=255)
    password = serializers.CharField(max_length=255)

class ProductSerializer(serializers.Serializer):
    barcode = serializers.CharField(max_length=255)
    asset_type = serializers.CharField(max_length=255)
    asset_name = serializers.CharField(max_length=255)
    purchase_date = serializers.DateField()
    asset_value = serializers.CharField(max_length=255)
    condition = serializers.CharField(max_length=255)
    # location = serializers.CharField(max_length=255)

class AssignSerializer(serializers.Serializer):
    barcode = serializers.CharField(max_length=255)
    return_date = serializers.DateField()
    username = serializers.CharField(max_length=100)
    
class AssetSerializer(serializers.ModelSerializer):
    class Meta:
        model = Asset
        fields = '__all__'  # or specify individual fields
from rest_framework import serializers
from django.contrib.auth.models import *
from .models import *

class LoginSerializer(serializers.Serializer):
    username = serializers.CharField(max_length=255)
    password = serializers.CharField(max_length=255)

class ProductSerializer(serializers.Serializer):
    barcode = serializers.CharField(max_length=255)
    product_name = serializers.CharField(max_length=255)
    product_price = serializers.DecimalField(max_digits=10, decimal_places=2)


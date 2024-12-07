from django.db import models
from datetime import timedelta

class stationDetails(models.Model):
    station_id = models.IntegerField(primary_key=True)
    station_name = models.CharField(max_length=255)
    station_code = models.CharField(max_length=255)

    def __str__(self):
        return self.station_name
    
class role(models.Model):
    role_id = models.IntegerField(primary_key=True)
    role = models.CharField(max_length=255)

    def __str__(self):
        return self.role

class User(models.Model):

    user_id = models.IntegerField(primary_key=True, unique=True)
    # user_image = models.ImageField(null=True, blank=True)
    username = models.CharField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    role = models.ForeignKey(role, null=True, blank=True, on_delete=models.SET_NULL)
    full_name = models.CharField(max_length=255)
    contact_number = models.CharField(max_length=15, null=True, blank=True)
    email = models.EmailField(unique=True, null=True, blank=True)
    station = models.ForeignKey(stationDetails, null=True, blank=True, on_delete=models.SET_NULL)
    last_login = models.DateTimeField(null=True, blank=True)
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return self.username
    
class AssetCategory(models.Model):
    category_id = models.IntegerField(primary_key=True)
    category_name = models.CharField(max_length=255)
    
    def __str__(self):
        return self.category_name
    
class AssetSubCategory(models.Model):
    sub_category_id = models.IntegerField(primary_key=True)
    category = models.ForeignKey(AssetCategory, null=True, blank=True, on_delete=models.SET_NULL)
    sub_category_name = models.CharField(max_length=255)
    
    def __str__(self):
        return self.sub_category_name
    

class Asset(models.Model):
    asset_id = models.IntegerField(primary_key=True, unique=True)
    asset_name = models.CharField(max_length=255)
    asset_category = models.ForeignKey(AssetSubCategory, null=True, blank=True, on_delete=models.SET_NULL)
    barcode = models.CharField(max_length=255, unique=True)
    purchase_date = models.DateField()
    asset_value = models.CharField(max_length=255)
    condition = models.CharField(max_length=255)
    location = models.CharField(max_length=255)
    assign_to = models.ForeignKey(User, null=True, blank=True, on_delete=models.SET_NULL)

    def __str__(self):
        return f"{self.barcode} - {self.asset_name}"
    
class Allocation(models.Model):          #issuedproducts
    allocation_id = models.IntegerField(primary_key=True, unique=True)
    asset_barcode = models.CharField(max_length=255)
    user = models.ForeignKey(User, null=True, blank=True, on_delete=models.SET_NULL)
    assign_date = models.DateField(auto_now_add=True)
    return_date = models.DateField(null=True, blank=True)
    assign_location = models.CharField(max_length=255)
    
    def __str__(self):
        return f"Asset : {self.asset_barcode} - Allocated to: {self.user}"
    
class RequestAsset(models.Model):
    request_id = models.IntegerField(primary_key=True, unique=True)
    user = models.ForeignKey(User, null=True, blank=True, on_delete=models.SET_NULL)
    asset = models.ForeignKey(Asset, null=True, blank=True, on_delete=models.SET_NULL)
    quantity = models.CharField(max_length=255)
    request_date = models.DateField(auto_now_add=True)
    request_location = models.CharField(max_length=255)
    status = models.CharField(max_length=255, default='Pending')
    
    def __str__(self):
        return f"Request : {self.request_id} - by: {self.user}"

class Maintenance(models.Model):
    maintenance_id = models.AutoField(primary_key=True)
    asset = models.ForeignKey("Asset", null=True, blank=True, on_delete=models.SET_NULL)
    last_maintenance_date = models.DateField()  # Tracks the last maintenance date
    next_maintenance_date = models.DateField()  # Tracks the next maintenance date
    cost = models.CharField(max_length=100)
    def save(self, *args, **kwargs):
        if not self.next_maintenance_date and self.last_maintenance_date:
            # Calculate the next maintenance date based on last maintenance date
            self.next_maintenance_date = self.last_maintenance_date + timedelta(days=180)
        super().save(*args, **kwargs)

    def __str__(self):
        return f"Maintenance for Asset: {self.asset} - Next Maintenance: {self.next_maintenance_date}"
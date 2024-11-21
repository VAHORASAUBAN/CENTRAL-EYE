from django.db import models

STATUS_CHOICES = [
    ('Active', 'Active'),
    ('Inactive', 'Inactive'),
]

class stationDetails(models.Model):
    station_id = models.IntegerField(primary_key=True)
    station_name = models.CharField(max_length=255)
    staion_code = models.CharField(max_length=255)

    def __str__(self):
        return self.station_name
    
class role(models.Model):
    role_id = models.IntegerField(primary_key=True)
    role = models.CharField(max_length=255)

    def __str__(self):
        return self.role

class User(models.Model):

    user_id = models.IntegerField(primary_key=True, unique=True)
    username = models.CharField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    role = models.OneToOneField(role, related_name='user_role', null=True, blank=True, on_delete=models.CASCADE)
    full_name = models.CharField(max_length=255)
    contact_number = models.CharField(max_length=15, null=True, blank=True)
    email = models.EmailField(unique=True, null=True, blank=True)
    station = models.OneToOneField(stationDetails, related_name='stationDetails', null=True, blank=True, on_delete=models.CASCADE,) # type: ignore
    status = models.CharField(max_length=50, choices=STATUS_CHOICES, default='Active')

    def __str__(self):
        return self.username
    
class AssetCategory(models.Model):
    category_id = models.IntegerField(primary_key=True)
    category_name = models.CharField(max_length=255)
    
    def __str__(self):
        return self.category_name
    

class Asset(models.Model):
    asset_id = models.IntegerField(primary_key=True, unique=True)
    asset_name = models.CharField(max_length=255)
    barcode = models.CharField(max_length=255, unique=True)
    asset_type = models.CharField(max_length=255)
    purchase_date = models.DateField()
    asset_value = models.CharField(max_length=255)
    condition = models.CharField(max_length=255)

    def __str__(self):
        return f"{self.barcode} - {self.asset_name}"
    
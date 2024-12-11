from django.db import models
from datetime import timedelta

ASSET_STATUS_CHOICES = [
    ('in-use', 'In Use'),
    ('available', 'Available'),
    ('expired', 'Expired'),
    ('in-maintenance', 'In Maintenance'),
]

CONDITION_CHOICES = [
    ('good', 'Good'),
    ('average', 'Average'),
    ('below-average', 'Below Average'),
]

REQUEST_STATUS_CHOICES = [
    ('Pending', 'Pending'),
    ('Approved', 'Approved'),
    ('Rejected', 'Rejected'),
]

class stationDetails(models.Model):
    station_id = models.IntegerField(primary_key=True)
    station_name = models.CharField(max_length=255)
    station_code = models.CharField(max_length=255)
    station_address = models.CharField(max_length=255)

    def __str__(self):
        return self.station_name
    
class role(models.Model):
    role_id = models.IntegerField(primary_key=True)
    role = models.CharField(max_length=255)

    def __str__(self):
        return self.role

class UserDetails(models.Model):

    user_id = models.IntegerField(primary_key=True, unique=True)
    # user_image = models.ImageField(null=True, blank=True)
    username = models.CharField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    role = models.ForeignKey(role, null=True, blank=True, on_delete=models.SET_NULL)
    first_name = models.CharField(max_length=255)
    last_name = models.CharField(max_length=255)
    contact_number = models.CharField(max_length=15, null=True, blank=True)
    email = models.EmailField(unique=True, null=True, blank=True)
    station = models.ForeignKey(stationDetails, null=True, blank=True, on_delete=models.SET_NULL)
    last_login = models.DateTimeField(null=True, blank=True)
    is_active = models.BooleanField(default=True)

    def __str__(self):
        return self.username
    
class Status(models.Model):
    status_id = models.IntegerField(primary_key=True, unique=True)
    status = models.CharField(max_length=255)
    
    def __str__(self):
        return self.status
     
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
    condition = models.CharField(max_length=20, choices=CONDITION_CHOICES, default='good')
    location = models.CharField(max_length=255)
    assign_to = models.ForeignKey(UserDetails, null=True, blank=True, on_delete=models.SET_NULL)
    asset_status = models.CharField(max_length=20, choices=ASSET_STATUS_CHOICES, default='available')
    

    def save(self, *args, **kwargs):
        # Check if the Asset is being created (not updated)
        is_new = self._state.adding

        # Get the previous status (if updating)
        try:
            existing_asset = Asset.objects.get(pk=self.pk)
            previous_status = existing_asset.asset_status
        except Asset.DoesNotExist:
            previous_status = None

        super().save(*args, **kwargs)  # Save the Asset instance first

        # Ensure only one Maintenance record is created for new assets
        if is_new and not Maintenance.objects.filter(asset=self).exists():
            Maintenance.objects.create(
                asset=self,
                last_maintenance_date=self.purchase_date,  # Optionally use purchase_date as the initial date
            )

        # Handle logic for adding to ExpiredProduct table
        if self.asset_status == 'expired' and previous_status != 'expired':
            ExpiredProduct.objects.get_or_create(asset=self)

        # Handle logic for removing from ExpiredProduct table
        elif self.asset_status != 'expired' and previous_status == 'expired':
            ExpiredProduct.objects.filter(asset=self).delete()

           # Handle logic for adding to MaintenanaceProduct table
        if self.asset_status == 'in-maintenance' and previous_status != 'in-maintenance':
            Maintenance.objects.get_or_create(asset=self)

        # Handle logic for removing from MaintenanaceProduct table
        elif self.asset_status != 'in-maintenance' and previous_status == 'in-maintenance':
            Maintenance.objects.filter(asset=self).delete()

    def __str__(self):
        return f"{self.barcode} - {self.asset_name} - {self.asset_status}"

    
class Allocation(models.Model):          #issuedproducts
    allocation_id = models.IntegerField(primary_key=True, unique=True)
    asset = models.ForeignKey(Asset, null=True, blank=True, on_delete=models.SET_NULL)
    user = models.ForeignKey(UserDetails, null=True, blank=True, on_delete=models.SET_NULL)
    assign_date = models.DateField(auto_now_add=True)
    expected_return_date = models.DateField(null=True, blank=True)
    return_date = models.DateField(null=True, blank=True)
    assign_location = models.CharField(max_length=255)
    
    def __str__(self):
        return f"Asset : {self.asset} - Barcode : {self.asset.barcode} - Allocated to: {self.user}"
    
class RequestAsset(models.Model):
    request_id = models.IntegerField(primary_key=True, unique=True)
    user = models.ForeignKey(UserDetails, null=True, blank=True, on_delete=models.SET_NULL)
    asset = models.ForeignKey(Asset, null=True, blank=True, on_delete=models.SET_NULL)
    quantity = models.CharField(max_length=255)
    return_date = models.DateField(null=True, blank=True)
    request_date = models.DateField(auto_now_add=True)
    request_location = models.CharField(max_length=255)
    request_status = models.CharField(max_length=20, choices=REQUEST_STATUS_CHOICES, default='Pending')
    
    def __str__(self):
        return f"Request : {self.request_id} - by: {self.user}"

class Maintenance(models.Model):
    maintenance_id = models.AutoField(primary_key=True)
    asset = models.ForeignKey("Asset", null=True, blank=True, on_delete=models.CASCADE)
    last_maintenance_date = models.DateField()  # Tracks the last maintenance date
    next_maintenance_date = models.DateField()  # Tracks the next maintenance date
    maintenance_cost = models.CharField(max_length=255)
    return_date = models.DateField(null=True, blank=True)  # Add this field to capture return date
            
    def save(self, *args, **kwargs):
        # Calculate the next maintenance date if not set
        if not self.next_maintenance_date and self.last_maintenance_date:
            self.next_maintenance_date = self.last_maintenance_date + timedelta(days=180)

        # Update return date and asset status when coming back from maintenance
        if self.return_date and self.asset:
            self.asset.asset_status = 'available'  # Update asset status to "available"
            self.asset.save()  # Save the asset changes

        super().save(*args, **kwargs)

    def __str__(self):
        return f"Maintenance for Asset: {self.asset} - Next Maintenance: {self.next_maintenance_date}"
    
class ExpiredProduct(models.Model):
    expired_id = models.AutoField(primary_key=True)
    asset = models.OneToOneField("Asset", on_delete=models.CASCADE, related_name="expired_record")
    expiration_date = models.DateField(auto_now_add=True)
    reason = models.CharField(max_length=255, default="Expired status set by admin")  # Optional field

    def __str__(self):
        return f"Expired Product: {self.asset.asset_name} (Barcode: {self.asset.barcode})"

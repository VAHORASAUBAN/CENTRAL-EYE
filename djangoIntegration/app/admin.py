from django.contrib import admin
from .models import *

# Register your models here.
admin.site.register(User)
admin.site.register(Asset)
admin.site.register(role)
admin.site.register(AssetCategory)
admin.site.register(AssetSubCategory)
admin.site.register(stationDetails)
admin.site.register(Allocation)
admin.site.register(RequestAsset)
admin.site.register(Maintenance)
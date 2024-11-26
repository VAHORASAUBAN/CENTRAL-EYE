from django.db.models.signals import post_save
from django.dispatch import receiver
from datetime import timedelta
from .models import Asset, Maintenance

@receiver(post_save, sender=Asset)
def create_maintenance_entry(sender, instance, created, **kwargs):
    if created:  # Check if this is a new Asset
        Maintenance.objects.create(
            asset=instance,
            last_maintenance_date=instance.purchase_date,
            next_maintenance_date=instance.purchase_date + timedelta(days=180)
        )
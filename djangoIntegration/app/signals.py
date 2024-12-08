# from django.db.models.signals import post_save
# from django.dispatch import receiver
# from .models import Asset, Maintenance

# @receiver(post_save, sender=Asset)
# def create_maintenance_entry(sender, instance, created, **kwargs):
#     if created:  # Ensure it runs only on creation
#         Maintenance.objects.get_or_create(
#             asset=instance,  # Use the saved instance here
#             defaults={
#                 'next_maintenance_date': instance.purchase_date,
#                 'maintenance_cost': '0',
#             }
#         )
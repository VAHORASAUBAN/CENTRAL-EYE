from django.db import models

class User(models.Model):
    USER_ROLES = [
        ('Admin', 'Admin'),
        ('Officer', 'Officer'),
        ('Inspector', 'Inspector'),
    ]

    STATUS_CHOICES = [
        ('Active', 'Active'),
        ('Inactive', 'Inactive'),
    ]

    user_id = models.AutoField(primary_key=True)
    username = models.CharField(max_length=255, unique=True)
    password = models.CharField(max_length=255)
    role = models.CharField(max_length=50, choices=USER_ROLES)
    full_name = models.CharField(max_length=255)
    contact_number = models.CharField(max_length=15, null=True, blank=True)
    email = models.EmailField(unique=True, null=True, blank=True)
    status = models.CharField(max_length=50, choices=STATUS_CHOICES, default='Active')

    def __str__(self):
        return self.username
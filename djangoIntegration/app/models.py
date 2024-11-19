from django.db import models

# Create your models here.
class signup(models.Model):
    userID = models.AutoField(unique=True, primary_key=True)
    username = models.CharField(unique=True, max_length=15)
    password = models.CharField(max_length=20)
    
    def _str_(self):
        return self.username
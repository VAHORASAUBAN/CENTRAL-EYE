# Generated by Django 5.1.3 on 2024-11-25 14:34

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0007_alter_maintenance_maintenance_id'),
    ]

    operations = [
        migrations.RenameField(
            model_name='asset',
            old_name='AssetCategory',
            new_name='asset_category',
        ),
    ]

# Generated by Django 5.1.3 on 2024-11-22 09:39

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0003_alter_allocation_user_alter_asset_assign_to'),
    ]

    operations = [
        migrations.AlterField(
            model_name='allocation',
            name='user',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.user'),
        ),
        migrations.AlterField(
            model_name='asset',
            name='assign_to',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.user'),
        ),
    ]

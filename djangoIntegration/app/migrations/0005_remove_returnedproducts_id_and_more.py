# Generated by Django 5.0.6 on 2024-12-12 11:39

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0004_asset_asset_add_date'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='returnedproducts',
            name='id',
        ),
        migrations.AddField(
            model_name='returnedproducts',
            name='allocation_id',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.allocation'),
        ),
        migrations.AddField(
            model_name='returnedproducts',
            name='return_id',
            field=models.AutoField(default=2, primary_key=True, serialize=False),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='returnedproducts',
            name='returnDate',
            field=models.DateField(auto_now=True, default=3),
            preserve_default=False,
        ),
    ]

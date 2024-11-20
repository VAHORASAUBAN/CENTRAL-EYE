# Generated by Django 5.1.3 on 2024-11-19 16:27

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0004_delete_product'),
    ]

    operations = [
        migrations.CreateModel(
            name='Product',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('barcode', models.CharField(max_length=255)),
                ('product_name', models.CharField(max_length=255)),
                ('product_price', models.DecimalField(decimal_places=2, max_digits=10)),
            ],
        ),
    ]

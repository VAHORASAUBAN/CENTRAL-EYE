# Generated by Django 5.1.3 on 2024-12-11 12:12

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0022_merge_20241211_1739'),
    ]

    operations = [
        migrations.AddField(
            model_name='requestasset',
            name='asset',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.asset'),
        ),
        migrations.AlterField(
            model_name='maintenance',
            name='asset',
            field=models.ForeignKey(blank=True, null=True, on_delete=django.db.models.deletion.SET_NULL, to='app.asset'),
        ),
    ]

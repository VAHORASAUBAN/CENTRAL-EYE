<<<<<<< HEAD
# Generated by Django 5.1.3 on 2024-12-11 12:12
=======
# Generated by Django 5.0.6 on 2024-12-11 15:41
>>>>>>> 514d1d1b50d491f06953704c409b56498dc921df

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
<<<<<<< HEAD
        ('app', '0022_merge_20241211_1739'),
=======
        ('app', '0022_merge_20241211_1541'),
>>>>>>> 514d1d1b50d491f06953704c409b56498dc921df
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
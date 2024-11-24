"""
URL configuration for djangoIntegration project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""

from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('api/login/', views.login_view, name='login'),
    path('api/add_product/', views.add_product, name='addProduct'),  # Update this line
    path('api/users/', views.user_list_view, name='userlist'),
     
    path('productlist', views.productlist, name='productlist'),
    path('addproduct', views.addproduct, name='addproduct'),
    path('editproduct', views.editproduct, name='editproduct'),

    path('categorylist', views.categorylist, name='categorylist'),
    path('addcategory', views.addcategory, name='addcategory'),
    path('editcategory', views.addcategory, name='editcategory'),

    path('importproduct', views.importproduct, name='importproduct'),
    path('barcode', views.barcode, name='barcode'),

    path('issuedproducts', views.issuedproducts, name='issuedproducts'),
    path('editissuedproducts', views.editissuedproducts, name='editissuedproducts'),
    path('addissuedproducts', views.addissuedproducts, name='addissuedproducts'),

    path('maintenanceproducts', views.maintenanceproducts, name='maintenanceproducts'),
    path('editmaintenanceproducts', views.editmaintenanceproducts, name='editmaintenanceproducts'),
    path('addmaintenanceproducts', views.addmaintenanceproducts, name='addmaintenanceproducts'),

    path('expiredproducts', views.expiredproducts, name='expiredproducts'),
    path('editexpiredproducts', views.editexpiredproducts, name='editexpiredproducts'),
    path('addexpiredproducts', views.addexpiredproducts, name='addexpiredproducts'),

    path('returnproducts', views.returnproducts, name='returnproducts'),
    path('editreturnproducts', views.editreturnproducts, name='editreturnproducts'),
    path('addreturnproducts', views.addreturnproducts, name='addreturnproducts'),


    path('newuser', views.newuser, name='newuser'),
    path('userlists', views.userlists, name='userlists'),
    path('edituser', views.edituser, name='edituser'),


    path('expenselist',views.expenseList, name='expenseList'),
    path('createexpense',views.createExpense, name='createExpense'),
    path('editexpense',views.editExpense, name='editExpense'),
    path('expensecategory',views.expenseCategory, name='expenseCategory'),
    path('quotationlist',views.quotationList, name='quotationList'),
    path('addquotation',views.addquotation, name='addquotation'),
    path('newstation',views.newstation, name='newstation'),
    path('stationlist',views.stationlist, name='stationlist'),
    path('editstation',views.editstation, name='editstation'),
    path('editquotation',views.editQuotation, name='editQuotation'),
    path('editexpense',views.editExpense, name='editExpense'),
    path('profile',views.profile, name='profile'),
    path('generalsettings',views.generalSettings, name='generalSettings'),
    path('signin',views.signin, name='signin'),
    path('forgetpassword',views.forgetpassword, name='forgetpassword'),
    path('signup',views.signup, name='signup'),
    
 
    path('api/assign_product/', views.assign_product, name='assignProduct'),  # Update this line
    # path('api/assets/', views.AssetList, name='asset-list'),
    path('api/assets/', views.AssetListView, name='asset-list'),
]

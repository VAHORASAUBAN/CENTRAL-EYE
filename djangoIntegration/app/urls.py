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
    path('api/login/', views.login_view, name='login'),
    path('api/add_product/', views.add_product, name='addProduct'),  # Update this line
    path('', views.index, name='index'),
    path('expenselist',views.expenseList, name='expenseList'),
    path('createexpense',views.createExpense, name='createExpense'),
    path('editexpense',views.editExpense, name='editExpense'),
    path('expensecategory',views.expenseCategory, name='expenseCategory'),
    path('quotationlist',views.quotationList, name='quotationList'),
    path('addquotation',views.addquotation, name='addquotation'),
    path('newcountry',views.newCountry, name='newCountry'),
    path('countrieslist',views.countriesList, name='countriesList'),
    path('editcountry',views.editCountry, name='editCountry'),
    path('editquotation',views.editQuotation, name='editQuotation'),
    path('editexpense',views.editExpense, name='editExpense'),
    path('profile',views.profile, name='profile'),
    path('generalsettings',views.generalSettings, name='generalSettings'),
    path('signin',views.signin, name='signin'),
    path('forgetpassword',views.forgetpassword, name='forgetpassword'),
    path('signup',views.signup, name='signup'),
    
 ]

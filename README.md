🛡️ CENTRAL-EYE

CENTRAL-EYE is a full-stack Hardware Inventory Management System designed for institutions like police departments to track, manage, and audit hardware assets efficiently. 
Built as part of Smart India Hackathon 2024 (PS ID: 1789), this solution ensures centralized, secure, and scalable asset handling using a Django-powered REST API, a Java-based Android App, and a Web-based Admin Panel.

---

 🔗 System Overview

This project contains three major components:

1. Android App (Java)  
   - Field-level asset tracking  
   - Barcode/QR code scanning  
   - Offline syncing with Django backend

2. Web App (HTML/CSS/JS)  
   - Admin dashboard  
   - Manual asset control  
   - Reports, user management, and audit logs

3. Django Backend (Python)  
   - REST APIs for data communication  
   - Auth, asset CRUD, audit trail, role-based access  
   - Acts as the central data and logic hub for Android & Web UI

---

 🧠 Key Features

- 🔒 Secure Login with role-based access (Admin, Officer, Auditor)
- 📦 Real-Time Asset Tracking via APIs
- 📱 Android App with QR/Barcode scanning
- 🌐 Responsive Web Dashboard for full control
- 🛠️ Maintenance & Lifecycle Management
- 🧾 Audit Trail & Activity Logs
- 📊 Automated Reports and Notifications

---

 🛠️ Tech Stack

| Layer        | Technology                 |
|--------------|----------------------------|
| Frontend     | Android (Java), HTML/CSS/JS|
| Backend      | Django, Django REST Framework |
| Database     | SQLite (Dev) / PostgreSQL (Prod) |
| API Format   | RESTful JSON               |
| Tools        | Android Studio, VSCode, Postman |

---

 📁 Repository Structure

CENTRAL-EYE/
├── app/  Android App Source Code
├── djangoIntegration/  Django Backend
├── UI/  Web UI (HTML/CSS/JS)
├── README.md  This file
└── .gitignore

---

 🚀 How to Run the Project

 1. Backend (Django API)

bash
cd djangoIntegration
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

 Migrate and run
python manage.py migrate
python manage.py runserver
Default API base URL: http://127.0.0.1:8000/api/

2. Android App
Open /app in Android Studio
Update API base URL in security_config.xml to match your local IP or deployed backend

Build and run the APK on a device/emulator

3. Web UI
Open /UI folder in VSCode or any code editor

Serve using Live Server or integrate with Django's static files

🏁 Future Scope
🌍 Geo-tagged hardware map view (GIS)

🔔 Push Notifications (FCM, email)

🧠 Predictive Maintenance using ML

🧩 Integration with government procurement portals

🤝 Contributing
Pull requests are welcome!

Steps to Contribute
bash
Copy
Edit
git clone https://github.com/VAHORASAUBAN/CENTRAL-EYE.git
cd CENTRAL-EYE
git checkout -b feature/your-feature
 Make your changes
git commit -m "Add your feature"
git push origin feature/your-feature
Open a Pull Request describing your changes.

📜 License
This project is licensed under the MIT License – feel free to use, modify, and distribute responsibly.

👏 Developed By
Team API-FETCHED
Finalists at Smart India Hackathon 2024
Problem Statement ID: 1789 – “Centralized Hardware Inventory Management System for Police Departments”

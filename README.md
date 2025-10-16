## 📂 Project Structure



```text
.
│   .gitignore
│   pom.xml
│   README.md
│
├───src
│   ├───main
│   │   ├───java
│   │   │   └───edu
│   │   │       └───escuelaing
│   │   │           └───app
│   │   │               │   App.java
│   │   │               │
│   │   │               ├───example
│   │   │               │       GreetingController.java
│   │   │               │       HelloController.java
│   │   │               │
│   │   │               └───framework
│   │   │                       Dispatcher.java
│   │   │                       GetMapping.java
│   │   │                       MicroSpringBoot.java
│   │   │                       Request.java
│   │   │                       RequestParam.java
│   │   │                       RestController.java
│   │   │                       SimpleHttpServer.java
│   │   │                       WebApp.java
│   │   │
│   │   └───resources
│   │       └───public
│   │           │   index.html
│   │           │   styles.css
│   │           │   app.js
│   │           └───images/
│   │                   LOGO.png
│   │                   LOGO2.jpg
│   │                   header-logoescuela.jpg
│   │                   ...


```
Arquitectura

Desarrollo Local

```
+----------------------+      +-----------------------+
|  Frontend (static)   | ---> |   Backend REST API    |
|  index.html / app.js |      |   /api/*              |
|  Port: 8080 (local)  |      |   Spring Boot         |
|  (serve local files) |      |   localhost:8080      |
+----------------------+      +-----------------------+
```


Producción (AWS) — implementación real que hicimos
```

+-------------------------------------------------------------------------------------------+
|                                        AWS CLOUD                                          |
|-------------------------------------------------------------------------------------------|
|                                                                                           |
|  Public Internet                                                                          |
|          |                                                                                |
|          v                                                                                |
|                                                                                           |
|  +-----------------------------------+        +-----------------------------------------+ |
|  |        EC2 - Apache (Front)       | <----> |        EC2 - Spring Boot (App)          | |
|  |-----------------------------------|        |-----------------------------------------| |
|  | IP: 3.140.247.200                 |        | Jar: /opt/demo/demo-0.0.1-SNAPSHOT.jar  | |
|  | vHost: arep.conf                  |        | systemd: demo.service                   | |
|  | Serves: /opt/demo/static          |        | Escucha: 127.0.0.1:8080                 | |
|  | TLS: Certbot / Let’s Encrypt      |        | JWT Secret: desde variable de entorno   | |
|  | Certs: /etc/letsencrypt/live/     |        | Bcrypt: para hashes                    | |
|  | ProxyPass /api -> 127.0.0.1:8080  |        |                                         | |
|  +-----------------------------------+        +-----------------------------------------+ |
|                                                                                           |
|  +-----------------------------------+                                                    |
|  |        Security Group (AWS)       |                                                    |
|  |-----------------------------------|                                                    |
|  | HTTP/HTTPS: Allow 80,443 from 0.0.0.0/0                                                |
|  | SSH (22): Only from TU_IP/32                                                          |
|  +-----------------------------------+                                                    |
|                                                                                           |
+-------------------------------------------------------------------------------------------+
```

Flujo de Datos (petición típica)
```
Browser (index.html/app.js)
     |
     | 1) POST /api/auth/register  (HTTPS -> Apache)
     v
Apache (reverse proxy, TLS terminated)
     |
     | 2) Proxy to http://127.0.0.1:8080/api/auth/register
     v
Spring Boot (AuthController)
     |
     | 3) UserService.register():
     |      - check repo (UserRepository)
     |      - hash password via BCrypt (encoder.encode)
     |      - save User (username, passwordHash)
     v
Database (H2 dev / RDBMS prod)
     |
     | 4) Login -> UserService.checkPassword():
     |      - encoder.matches(rawPassword, storedHash)
     |      - if ok -> JwtUtil.generateToken(username)
     v
Browser receives JWT -> store (localStorage) -> calls protected APIs
     |
     | 5) Subsequent request: Authorization: Bearer <TOKEN>
     v
Apache proxies /api -> JwtFilter in Spring validates token and sets SecurityContext

```

Implementación (específica para tu proyecto)
Prerrequisitos

Java 17+ / 21 (tú usaste Java 21)

Maven 3.6+

Git

Cuenta AWS con permisos para EC2 / Security Groups

Cliente SSH (OpenSSH) y la clave .pem descargada

En la instancia EC2: apache2, certbot (snap), openjdk, git, maven (opcional si compilás en la instancia)



1) Ejecución local (desarrollo / pruebas)

En tu PC Windows (carpeta del proyecto demo):

Compilar y empaquetar JAR:

<img width="1251" height="666" alt="image" src="https://github.com/user-attachments/assets/56e227de-fff4-41b2-80ab-4622f8600f02" />

```

cd C:\Users\AGsan\Downloads\demo (2)\demo
mvn clean package -DskipTests
# o con wrapper:
.\mvnw clean package -DskipTests
# JAR resultará en target/demo-0.0.1-SNAPSHOT.jar
```
<img width="731" height="167" alt="image" src="https://github.com/user-attachments/assets/2d8a7495-72d8-489c-b03f-e2f41410725a" />

Ejecutar localmente (ejemplo):
<img width="1104" height="466" alt="image" src="https://github.com/user-attachments/assets/716956d5-5b25-45b1-9840-373ab4c27967" />

```
# define un secreto JWT localmente (solo dev)
set APP_JWT_SECRET=REPLACE_ME_BASE64_SECRET     # Windows cmd
# o PowerShell:
$env:APP_JWT_SECRET="REPLACE_ME_BASE64_SECRET"

# ejecutar
java -jar target\demo-0.0.1-SNAPSHOT.jar

```

2) Preparar EC2 en AWS (resumen rápido)

AMI: Ubuntu 24.04 LTS (usaste esa).

Tipo: t3.micro (capa gratuita).

Security Group: permitir inbound 80 (HTTP), 443 (HTTPS), 22 (SSH) — SSH restringido a tu IP. NO exponer 8080 públicamente (solo localhost).

Key Pair: descarga .pem y protégela.

<img width="1329" height="587" alt="image" src="https://github.com/user-attachments/assets/52322384-2b0f-4f01-831a-7850b162b7b7" />

4) Instalar Java, Apache y desplegar app en EC2 (comandos en la instancia)

Conéctate por ssh:


<img width="751" height="79" alt="image" src="https://github.com/user-attachments/assets/40fa51f3-56bc-44ff-b80d-96239eed0e1e" />
```

ssh -i "YOUR_KEY.pem" ubuntu@3.140.247.200
```

En la instancia:
```
# actualizar e instalar
sudo apt update && sudo apt upgrade -y
sudo apt install -y openjdk-21-jre-headless apache2 unzip curl

```

Mover JAR a /opt/demo y crear usuario
```
sudo mkdir -p /opt/demo
sudo useradd -r -s /usr/sbin/nologin demoapp || true
sudo mv /tmp/demo-0.0.1-SNAPSHOT.jar /opt/demo/demo-0.0.1-SNAPSHOT.jar
sudo chown -R demoapp:demoapp /opt/demo
```

Configurar Apache como proxy inverso y servir frontend

```
sudo mkdir -p /opt/demo/static
# si ya tienes index.html y app.js local, súbelos
# ejemplo scp para index.html:
scp -i "YOUR_KEY.pem" src/main/resources/static/index.html ubuntu@3.140.247.200:/tmp/
scp -i "YOUR_KEY.pem" src/main/resources/static/app.js ubuntu@3.140.247.200:/tmp/

sudo mv /tmp/index.html /opt/demo/static/index.html
sudo mv /tmp/app.js /opt/demo/static/app.js
sudo chown -R www-data:www-data /opt/demo/static
sudo chmod -R 755 /opt/demo/static
```

Obtener certificado Let’s Encrypt (sin comprar dominio) — usar nip.io
Instalar certbot (snap) y ejecutar:
```
sudo snap install core; sudo snap refresh core
sudo snap install --classic certbot
sudo ln -s /snap/bin/certbot /usr/bin/certbot

# pedir certificado (reemplaza dominio nip.io con tu IP)
```
sudo certbot --apache -d 3.140.247.200.nip.io
```
vhost HTTPS

<img width="743" height="576" alt="image" src="https://github.com/user-attachments/assets/3b8a3950-d8b1-494f-9fea-db44e1af9dc2" />

vhost HTTPS

<img width="779" height="569" alt="image" src="https://github.com/user-attachments/assets/e96b9db6-632e-4474-8d01-fdb84ed4440e" />


```
<IfModule mod_ssl.c>
<VirtualHost *:443>
    ServerName 3.140.247.200.nip.io
    ServerAlias www.3.140.247.200.nip.io

    DocumentRoot /opt/demo/static
    <Directory /opt/demo/static>
        Require all granted
        Options -Indexes
    </Directory>

    SSLProxyEngine On
    ProxyPreserveHost On
    ProxyPass /api http://127.0.0.1:8080/api
    ProxyPassReverse /api http://127.0.0.1:8080/api

    Header always set Strict-Transport-Security "max-age=31536000; includeSubDomains; preload"
    Header always set X-Content-Type-Options "nosniff"
    Header always set X-Frame-Options "DENY"
    Header always set Referrer-Policy "no-referrer"
    Header always set Content-Security-Policy "default-src 'self'; script-src 'self';"

    SSLCertificateFile /etc/letsencrypt/live/3.140.247.200.nip.io/fullchain.pem
    SSLCertificateKeyFile /etc/letsencrypt/live/3.140.247.200.nip.io/privkey.pem
    Include /etc/letsencrypt/options-ssl-apache.conf
    ErrorLog ${APACHE_LOG_DIR}/arep-ssl-error.log
    CustomLog ${APACHE_LOG_DIR}/arep-ssl-access.log combined
</VirtualHost>
</IfModule>
```

Recargar apache:



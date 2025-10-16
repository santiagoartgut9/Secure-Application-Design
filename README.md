
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen)
![Apache](https://img.shields.io/badge/Apache-2.4-red)
![AWS EC2](https://img.shields.io/badge/AWS-EC2-yellow)
![Let's Encrypt](https://img.shields.io/badge/TLS-Let%27s%20Encrypt-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-green)
![BCrypt](https://img.shields.io/badge/Encryption-BCrypt-lightgrey)
![License](https://img.shields.io/badge/License-MIT-blue)

# 🚀 Proyecto Spring Boot + Apache + AWS EC2

**Resumen rápido:** Aplicación Spring Boot (Java 21) desplegada en EC2. Apache actúa como reverse-proxy y sirve front estático; TLS con Let's Encrypt; autenticación con JWT; contraseñas con BCrypt.

---

## 📋 Tabla de contenidos
- [Descripción](#📝-descripción)
- [Estructura del proyecto](#📂-estructura-del-proyecto)
- [Arquitectura](#🏗️-arquitectura)
- [Flujo de datos](#🔁-flujo-de-datos)
- [Endpoints principales](#🧪-endpoints-principales)
- [Seguridad y certificación](#🔐-seguridad-y-certificación)
- [Prerrequisitos](#⚙️-prerrequisitos)
- [Ejecución local](#💻-ejecución-local)
- [Despliegue en EC2 (resumen y comandos)](#☁️-despliegue-en-ec2)
- [Apache vhost (HTTPS) — ejemplo de configuración](#📡-apache-vhost-https)
- [Comandos de verificación / logs](#📜-comandos-de-verificación)
- [Screenshots & evidencia](#🖼️-screenshots--evidencia)
- [Licencia y autor](#📄-licencia-y-autor)

---

## 📝 Descripción
Aplicación demo con backend REST en Spring Boot que proporciona endpoints de autenticación (registro/login) y APIs protegidas por JWT. Apache funciona como frontal con TLS (Let's Encrypt) y proxy hacia la app que escucha en `127.0.0.1:8080`.

---

## 📂 Estructura del proyecto

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
│   │   └───resources
│   │       └───public
│   │           │   index.html
│   │           │   styles.css
│   │           │   app.js
│   │           └───images/
│   │                   LOGO.png
│   │                   LOGO2.jpg
│   │                   header-logoescuela.jpg


```


![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.2-brightgreen)
![Apache](https://img.shields.io/badge/Apache-2.4-red)
![AWS EC2](https://img.shields.io/badge/AWS-EC2-yellow)
![Let's Encrypt](https://img.shields.io/badge/TLS-Let%27s%20Encrypt-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-green)
![BCrypt](https://img.shields.io/badge/Encryption-BCrypt-lightgrey)
![License](https://img.shields.io/badge/License-MIT-blue)

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

🧪 Endpoints principales

| Método | Endpoint             | Descripción                           | Autenticación |
| :----- | :------------------- | :------------------------------------ | :------------ |
| `POST` | `/api/auth/register` | Registra nuevo usuario (BCrypt hash)  | ❌             |
| `POST` | `/api/auth/login`    | Retorna token JWT válido              | ❌             |
| `GET`  | `/api/hello`         | Prueba token JWT (“Hello {username}”) | ✅             |


📊 Seguridad y Certificación

| Componente              | Validación                                           |
| ----------------------- | ---------------------------------------------------- |
| **Certificado SSL**     | Emitido por Let’s Encrypt, válido hasta *2026-01-13* |
| **Cifrado HTTPS**       | TLSv1.3 con `TLS_AES_256_GCM_SHA384`                 |
| **Password Storage**    | Hash mediante `BCryptPasswordEncoder`                |
| **Autenticación**       | JWT firmado con secreto (`APP_JWT_SECRET`)           |
| **Protecciones Apache** | HSTS, CSP, X-Frame-Options, Referrer-Policy          |
| **Puertos abiertos**    | 22 (SSH limitado), 80/443 (públicos), 8080 (privado) |


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
```
sudo systemctl reload apache2
sudo apache2ctl -S
sudo apache2ctl configtest

```


Asegurar red: cerrar puerto 8080

En AWS Console → EC2 → Security Groups (launch-wizard-1), revoca cualquier regla que abra 8080 a 0.0.0.0/0.

Deja 80 y 443 abiertos públicamente.

SSH (22) solo a tu IP

```
# 8080 debe fallar (si está cerrado)
curl -v http://3.140.247.200:8080/ || echo "Puerto 8080 no accesible desde internet"
# HTTP -> debe redirigir a HTTPS
curl -v http://3.140.247.200.nip.io/
# HTTPS -> debe servir tu app estática
curl -vk https://3.140.247.200.nip.io/
```

<img width="1055" height="519" alt="image" src="https://github.com/user-attachments/assets/203fb0b9-6fa8-40cd-8ed8-dda7f852481e" />

<img width="551" height="483" alt="image" src="https://github.com/user-attachments/assets/5f46c384-d57e-45fc-85c5-15a98bd776e9" />

Logs y evidencia
```
# Certificado
sudo certbot certificates
sudo certbot renew --dry-run

# Apache & vhosts
sudo apache2ctl -S
sudo apache2ctl configtest
sudo systemctl status apache2 --no-pager

# Cabeceras de seguridad
curl -sI https://3.140.247.200.nip.io | egrep -i "Strict-Transport-Security|Content-Security-Policy|X-Frame-Options|X-Content-Type-Options|Referrer-Policy"

# Registro / Login / Hello
curl -v -H "Content-Type: application/json" -d '{"username":"santiago","password":"Pass1234"}' https://3.140.247.200.nip.io/api/auth/register
curl -v -H "Content-Type: application/json" -d '{"username":"santiago","password":"Pass1234"}' https://3.140.247.200.nip.io/api/auth/login
# token -> llamar hello
curl -v -H "Authorization: Bearer $TOKEN" https://3.140.247.200.nip.io/api/hello

# Logs
sudo journalctl -u demo.service -n 200 --no-pager
sudo tail -n 200 /var/log/apache2/error.log
sudo tail -n 200 /var/log/apache2/access.log
```












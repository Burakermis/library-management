# Library Management System

Library Management System, kütüphane yönetimi için geliştirilmiş bir uygulamadır. Kullanıcılar, kitapları yönetebilir, ödünç alabilir, ve güvenli bir şekilde işlemlerini gerçekleştirebilir. JWT tabanlı kimlik doğrulama ve rol bazlı yetkilendirme ile sistemin güvenliği sağlanmıştır.

## Özellikler

### Kullanıcı Yönetimi:
- Kullanıcı kaydı, giriş ve güncelleme işlemleri
- Kullanıcı silme ve detay görüntüleme
- Kullanıcı rolleri (ADMIN, LIBRARIAN, READER)

### Kitap Yönetimi:
- Kitap ekleme, silme, güncelleme ve listeleme

### Ödünç Alma İşlemleri:
- Kitap ödünç alma ve iade etme
- Kullanıcı ödünç alma geçmişi
- Gecikmiş kitapların listelenmesi

### Güvenlik:
- JWT tabanlı kimlik doğrulama
- Rol bazlı yetkilendirme

### API Dokümantasyonu:
- Swagger/OpenAPI ile API dokümantasyonu

### Veritabanı Desteği:
- PostgreSQL

## Teknoloji Yığını

- **Java 17**
- **Spring Boot 3.x**
- **Maven**
- **JWT (JSON Web Token)**
- **PostgreSQL**
- **Swagger/OpenAPI**
- **Lombok**

## Kurulum ve Çalıştırma

### Gereksinimler
- Java 17 veya üzeri
- Maven 3.8 veya üzeri
- PostgreSQL

### Adımlar

1. **Projeyi klonlayın:**

    ```bash
    git clone https://github.com/Burakermis/library-management-system.git
    cd library-management-system
    ```

2. **Gerekli bağımlılıkları yükleyin:**

    ```bash
    mvn clean install
    ```

3. **application.properties dosyasını yapılandırın:**
    - Üretim ortamı için PostgreSQL bilgilerini girin.

4. **Uygulamayı çalıştırın:**

    ```bash
    mvn spring-boot:run
    ```

5. **Swagger dokümantasyonuna erişin:**

   [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## API Endpoint'leri

### Kullanıcı Yönetimi
- `POST /api/v0/users/register` - Yeni kullanıcı kaydı
- `POST /api/v0/users/login` - Kullanıcı girişi
- `POST /api/v0/users/create` - Yeni kullanıcı oluşturma (ADMIN)
- `PUT /api/v0/users/{id}` - Kullanıcı güncelleme
- `DELETE /api/v0/users/{id}` - Kullanıcı silme (ADMIN)
- `GET /api/v0/users/{id}` - Kullanıcı detayları (ADMIN, LIBRARIAN)

### Kitap Yönetimi
- `POST /api/v0/books` - Yeni kitap ekleme (LIBRARIAN)
- `PUT /api/v0/books/{id}` - Kitap güncelleme (LIBRARIAN)
- `DELETE /api/v0/books/{id}` - Kitap silme (LIBRARIAN)
- `GET /api/v0/books` - Tüm kitapları listeleme

### Ödünç Alma İşlemleri
- `POST /api/v0/borrowings/borrow` - Kitap ödünç alma (READER)
- `PUT /api/v0/borrowings/return/{id}` - Kitap iade etme (READER)
- `GET /api/v0/borrowings/user/{id}` - Kullanıcı ödünç alma geçmişi (READER)
- `GET /api/v0/borrowings/overdue` - Gecikmiş kitaplar (LIBRARIAN)

## Testler

JUnit 5 ve Mockito kullanılarak birim testleri yazılmıştır.

```bash
mvn test
```

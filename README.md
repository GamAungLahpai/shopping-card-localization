# Shopping Cart with Database Localization

## Overview
This project is a JavaFX shopping cart application with MariaDB-based localization.

- UI text is loaded from the `localization_strings` table (not ResourceBundle files).
- Cart calculations are saved to `cart_records`.
- Item details are saved to `cart_items` with a foreign key to `cart_records`.

## Supported Languages
1. English (`en_US`)
2. Finnish (`fi_FI`)
3. Swedish (`sv_SE`)
4. Japanese (`ja_JP`)
5. Arabic (`ar_AR`, right-to-left layout)

## Database Setup (MariaDB)
Run the SQL script to create schema and seed localization data:

```bash
mysql -u <DB_USERNAME> -p < src/main/resources/database.sql
```

## Database Configuration
The application reads DB configuration from environment variables:

- `DB_URL` (example: `jdbc:mariadb://localhost:3306/shopping_cart_localization`)
- `DB_USER`
- `DB_PASSWORD`

Example (macOS/Linux):

```bash
export DB_URL="jdbc:mariadb://localhost:3306/shopping_cart_localization"
export DB_USER="app_user_example"
export DB_PASSWORD="example_password_123"
```

## Run Locally
```bash
mvn clean package
mvn javafx:run
```

## Run Tests
```bash
mvn test
```

## Docker
Build image:

```bash
docker build -t shopping-cart-localization:latest .
```

Run container (set DB connection to your MariaDB instance):

```bash
docker run --rm \
  -e DB_URL="jdbc:mariadb://host.docker.internal:3306/shopping_cart_localization" \
  -e DB_USER="app_user_example" \
  -e DB_PASSWORD="example_password_123" \
  shopping-cart-localization:latest
```

Push to Docker Hub:

```bash
docker login
docker tag shopping-cart-localization:latest your-dockerhub-username-example/shopping-cart:latest
docker push your-dockerhub-username-example/shopping-cart:latest
```

Pull and run from Docker Hub:

```bash
docker pull your-dockerhub-username-example/shopping-cart:latest
docker run --rm \
  -e DB_URL="jdbc:mariadb://host.docker.internal:3306/shopping_cart_localization" \
  -e DB_USER="app_user_example" \
  -e DB_PASSWORD="example_password_123" \
  your-dockerhub-username-example/shopping-cart:latest
```

## Verification Queries
Check localization rows by language:

```bash
mysql -u <DB_USERNAME> -p -e "USE shopping_cart_localization; SELECT language, COUNT(*) AS total FROM localization_strings GROUP BY language ORDER BY language;"
```

Check saved cart records:

```bash
mysql -u <DB_USERNAME> -p -e "USE shopping_cart_localization; SELECT id, total_items, total_cost, language, created_at FROM cart_records ORDER BY id DESC LIMIT 10;"
```

Check saved cart items:

```bash
mysql -u <DB_USERNAME> -p -e "USE shopping_cart_localization; SELECT id, cart_record_id, item_number, price, quantity, subtotal FROM cart_items ORDER BY id DESC LIMIT 20;"
```

# Shopping Cart with Localization

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
```bash
# Build
docker build -t shopping-cart .

# Run
docker run -it shopping-cart

docker login

# Push to Docker Hub
docker tag shopping-cart 218468/shopping-cart:latest
docker push 218468/shopping-cart:latest
```

## Pull and Run from Docker Hub
```bash
docker pull 218468/shopping-cart
docker run -it 218468/shopping-cart
```

## Languages
1. English
2. Finnish
3. Swedish
4. Japanese
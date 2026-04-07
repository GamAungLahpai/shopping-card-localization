CREATE DATABASE IF NOT EXISTS shopping_cart_localization
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE shopping_cart_localization;

CREATE TABLE IF NOT EXISTS cart_records (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            total_items INT NOT NULL,
                                            total_cost DOUBLE NOT NULL,
                                            language VARCHAR(10),
                                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          cart_record_id INT,
                                          item_number INT NOT NULL,
                                          price DOUBLE NOT NULL,
                                          quantity INT NOT NULL,
                                          subtotal DOUBLE NOT NULL,
                                          FOREIGN KEY (cart_record_id) REFERENCES cart_records(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS localization_strings (
                                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                                    `key` VARCHAR(100) NOT NULL,
                                                    value VARCHAR(255) NOT NULL,
                                                    language VARCHAR(10) NOT NULL,
                                                    UNIQUE KEY uk_localization_key_language (`key`, language)
);

INSERT INTO localization_strings (`key`, value, language) VALUES

                                                              ('select.language', 'Select Language', 'en_US'),
                                                              ('number.of.items', 'Number of items', 'en_US'),
                                                              ('create.items', 'Create Items', 'en_US'),
                                                              ('calculate', 'Calculate', 'en_US'),
                                                              ('enter.price', 'Enter price for item', 'en_US'),
                                                              ('enter.quantity', 'Enter quantity for item', 'en_US'),
                                                              ('total.cost', 'Total cost:', 'en_US'),
                                                              ('item.label', 'Item', 'en_US'),
                                                              ('item.total', 'Item total:', 'en_US'),

                                                              ('select.language', 'اختر اللغة', 'ar_AR'),
                                                              ('number.of.items', 'أدخل عدد العناصر', 'ar_AR'),
                                                              ('create.items', 'إنشاء العناصر', 'ar_AR'),
                                                              ('calculate', 'احسب', 'ar_AR'),
                                                              ('enter.price', 'أدخل سعر العنصر:', 'ar_AR'),
                                                              ('enter.quantity', 'أدخل كمية العنصر:', 'ar_AR'),
                                                              ('total.cost', 'التكلفة الإجمالية:', 'ar_AR'),
                                                              ('item.label', 'عنصر', 'ar_AR'),
                                                              ('item.total', 'إجمالي سعر العنصر:', 'ar_AR'),

                                                              ('select.language', 'Valitse kieli', 'fi_FI'),
                                                              ('number.of.items', 'Tuotteiden määrä', 'fi_FI'),
                                                              ('create.items', 'Luo tuotteet', 'fi_FI'),
                                                              ('calculate', 'Laske', 'fi_FI'),
                                                              ('enter.price', 'Syötä tuotteen hinta', 'fi_FI'),
                                                              ('enter.quantity', 'Syötä tuotteen määrä', 'fi_FI'),
                                                              ('total.cost', 'Kokonaishinta:', 'fi_FI'),
                                                              ('item.label', 'Tuote', 'fi_FI'),
                                                              ('item.total', 'Tuotteen hinta yhteensä:', 'fi_FI'),

                                                              ('select.language', '言語を選択', 'ja_JP'),
                                                              ('number.of.items', '購入する商品の数を入力してください', 'ja_JP'),
                                                              ('create.items', '商品を作成する', 'ja_JP'),
                                                              ('calculate', '計算する', 'ja_JP'),
                                                              ('enter.price', '商品の価格を入力してください:', 'ja_JP'),
                                                              ('enter.quantity', '商品の数量を入力してください:', 'ja_JP'),
                                                              ('total.cost', '合計金額:', 'ja_JP'),
                                                              ('item.label', '商品', 'ja_JP'),
                                                              ('item.total', '商品の合計金額:', 'ja_JP'),

                                                              ('select.language', 'Välj språk', 'sv_SE'),
                                                              ('number.of.items', 'Antal varor', 'sv_SE'),
                                                              ('create.items', 'Skapa varor', 'sv_SE'),
                                                              ('calculate', 'Beräkna', 'sv_SE'),
                                                              ('enter.price', 'Ange priset för varan', 'sv_SE'),
                                                              ('enter.quantity', 'Ange mängden varor', 'sv_SE'),
                                                              ('total.cost', 'Total kostnad:', 'sv_SE'),
                                                              ('item.label', 'Vara', 'sv_SE'),
                                                              ('item.total', 'Varans totalpris:', 'sv_SE');
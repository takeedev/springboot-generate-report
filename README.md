# Springboot-Generate-Report
Learning Generate Report And Download File

### URL to open Swagger
> localhost:8080/swagger
### OR 
> localhost:8080/swagger-ui/index.html

# Excel Export Performance Guide (Apache POI)
### libraries
```text
 <dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.4.1</version>
</dependency>
```
สรุปความแตกต่างระหว่าง `XSSFWorkbook` และ `SXSSFWorkbook`  
เพื่อช่วยเลือกวิธีสร้างไฟล์ Excel ให้เหมาะกับปริมาณข้อมูลและ performance

---

## 1. XSSFWorkbook

`XSSFWorkbook` เป็นการสร้างไฟล์ Excel แบบเก็บข้อมูลทั้งหมดไว้ในหน่วยความจำ (RAM)

### ลักษณะ
- เก็บทุก row / cell ไว้ใน heap
- รองรับการอ่าน–เขียน และแก้ไขย้อนหลัง
- รองรับ `autoSizeColumn`
- เหมาะกับไฟล์ขนาดเล็กถึงกลาง

### ข้อดี
- ใช้งานง่าย
- ฟีเจอร์ครบ
- แก้ cell ย้อนหลังได้
- รองรับ style และ formula เต็มรูปแบบ

### ข้อเสีย
- ใช้ RAM สูง
- เมื่อข้อมูลมาก (30,000+ rows) จะช้า
- มีโอกาสเกิด `OutOfMemoryError`

### เหมาะกับกรณี
- จำนวนแถว < 20,000
- ต้องใช้ `autoSizeColumn`
- ต้องปรับข้อมูลย้อนหลัง
- ไฟล์มี style จำนวนมาก

---

## 2. SXSSFWorkbook

`SXSSFWorkbook` เป็นโหมด streaming สำหรับเขียนไฟล์ Excel  
ออกแบบมาเพื่อรองรับข้อมูลจำนวนมากโดยใช้หน่วยความจำน้อย

```java
Workbook workbook = new SXSSFWorkbook(100);

from flask import Flask, request, jsonify
import cv2
import numpy as np
import os
from werkzeug.utils import secure_filename

app = Flask(__name__)

UPLOAD_FOLDER = "uploads"
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

def analyze_crop(image_path):
    img = cv2.imread(image_path)

    if img is None:
        return 0, "D", "Invalid Image"

    hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

    lower_green = np.array([25, 40, 40])
    upper_green = np.array([85, 255, 255])

    mask = cv2.inRange(hsv, lower_green, upper_green)

    green_pixels = np.sum(mask > 0)
    total_pixels = img.shape[0] * img.shape[1]

    health_percentage = (green_pixels / total_pixels) * 100

    if health_percentage >= 90:
        grade = "A"
        disease = "Healthy"
    elif health_percentage >= 75:
        grade = "B"
        disease = "Minor Issues"
    elif health_percentage >= 50:
        grade = "C"
        disease = "Moderate Disease"
    else:
        grade = "D"
        disease = "Severe Disease"

    return round(health_percentage, 2), grade, disease


@app.route("/analyze", methods=["POST"])
def analyze():
    if "image" not in request.files:
        return jsonify({"error": "No image uploaded"}), 400

    file = request.files["image"]

    filename = secure_filename(file.filename)
    path = os.path.join(UPLOAD_FOLDER, filename)

    file.save(path)

    health, grade, disease = analyze_crop(path)

    return jsonify({
        "health": health,
        "grade": grade,
        "disease": disease
    })
@app.route('/test', methods=['GET'])
def test():
    return jsonify({"message": "Server working"})

if __name__ == "__main__":
    app.run(port=5000, debug=True)

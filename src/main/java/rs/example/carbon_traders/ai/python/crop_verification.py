import sys
import random

image_path = sys.argv[1]
crop_type = sys.argv[2]

# MOCK AI LOGIC (Replace with ML later)
confidence = random.uniform(0.75, 0.98)

if confidence > 0.85:
    print(f"VERIFIED:{crop_type}:CONFIDENCE={confidence:.2f}")
else:
    print(f"REJECTED:{crop_type}:CONFIDENCE={confidence:.2f}")

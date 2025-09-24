import torch
from TTS.api import TTS

# 1. Force CPU to avoid device mismatch errors
device = "cpu"
print(f"Using device: {device}")

# 2. Load a Coqui-TTS VITS model
model_name = "tts_models/fr/css10/vits"
print(f"Loading model: {model_name}")
tts = TTS(model_name=model_name).to(device)

# 3. Export the model to ONNX
output_path = f"{model_name.replace('/', '_')}.onnx"
print(f"Exporting model to {output_path}...")

try:
    # Access the underlying VITS model
    vits_model = tts.synthesizer.tts_model

    # Export the model using its own export method
    vits_model.export_onnx(output_path)

    print("Model exported to ONNX successfully!")
except Exception as e:
    print(f"Error during ONNX export: {e}")

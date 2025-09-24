from TTS.api import TTS

# 1. Load a Coqui-TTS VITS model
model_name = "tts_models/fr/css10/vits"
print(f"Loading model: {model_name}")
tts = TTS(model_name=model_name)

# 2. Access the tokenizer
tokenizer = tts.synthesizer.tts_model.tokenizer

# 3. Get the vocabulary from the Graphemes object
if hasattr(tokenizer, 'characters') and hasattr(tokenizer.characters, 'vocab'):
    with open("tokens.txt", "w", encoding="utf-8") as f:
        for token in tokenizer.characters.vocab:
            f.write(token + "\n")
    print("tokens.txt created successfully.")
else:
    print("Could not find 'vocab' attribute on the 'Graphemes' object.")
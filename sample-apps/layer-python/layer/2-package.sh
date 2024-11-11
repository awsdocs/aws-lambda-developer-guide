mkdir python
cp -r create_layer/lib python/
zip -r layer_content.zip python -x "*/__pycache__/*" -x "*/pip*/*"

mkdir -p ruby/gems/3.3.0
cp -r vendor/bundle/ruby/3.3.0/* ruby/gems/3.3.0/
zip -r layer_content.zip ruby 

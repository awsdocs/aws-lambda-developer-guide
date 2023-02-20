# Lambda console<a name="foundation-console"></a>

You can use the Lambda console to configure applications, functions, code signing configurations, and layers\.

**Topics**
+ [Applications](#foundation-applications)
+ [Functions](#foundation-functions)
+ [Code signing](#foundation-code-signing)
+ [Layers](#foundation-layers)
+ [Edit code using the console editor](#code-editor)

## Applications<a name="foundation-applications"></a>

The [Applications](deploying-lambda-apps.md) page shows you a list of applications that have been deployed using AWS CloudFormation, or other tools including the AWS Serverless Application Model\. Filter to find applications based on keywords\. 

## Functions<a name="foundation-functions"></a>

The functions page shows you a list of functions defined for your account in this region\. The initial console flow to create a function depends on whether the function uses a [container image](gettingstarted-images.md) or [\.zip file archive](configuration-function-zip.md) for the deployment package\. Many of the optional [configuration tasks](configuration-function-common.md) are common to both types of function\.

The console provides a [code editor](#code-editor) for your convenience\. 

## Code signing<a name="foundation-code-signing"></a>

You can attach a [code signing](configuration-codesigning.md) configuration to a function\. With code signing, you can ensure that the code has been signed by an approved source and has not been altered since signing, and that the code signature has not expired or been revoked\. 

## Layers<a name="foundation-layers"></a>

Create [layers](configuration-layers.md) to separate your \.zip archive function code from its dependencies\. A layer is a ZIP archive that contains libraries, a custom runtime, or other dependencies\. With layers, you can use libraries in your function without needing to include them in your deployment package\. 

## Edit code using the console editor<a name="code-editor"></a>

You can use the code editor in the AWS Lambda console to write, test, and view the execution results of your Lambda function code\. The code editor supports languages that do not require compiling, such as Node\.js and Python\. The code editor supports only \.zip archive deployment packages, and the size of the deployment package must be less than 3 MB\. 

The code editor includes the *menu bar*, *windows*, and the *editor pane*\.



![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor.png)

For a list of what the commands do, see the [Menu commands reference](https://docs.aws.amazon.com/cloud9/latest/user-guide/menu-commands.html) in the *AWS Cloud9 User Guide*\. Note that some of the commands listed in that reference are not available in the code editor\.

**Topics**
+ [Working with files and folders](#code-editor-files)
+ [Working with code](#code-editor-code)
+ [Working in fullscreen mode](#code-editor-fullscreen)
+ [Working with preferences](#code-editor-prefs)

### Working with files and folders<a name="code-editor-files"></a>

You can use the **Environment** window in the code editor to create, open, and manage files for your function\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-env.png)

**To show or hide the Environment window**, choose the **Environment** button\. If the **Environment** button is not visible, choose **Window, Environment** on the menu bar\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-env-button.png)

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-env-menu.png)



**To open a single file and show its contents in the editor pane**, double\-click the file in the **Environment** window\.

**To open multiple files and show their contents in the editor pane**, choose the files in the **Environment** window\. Right\-click the selection, and then choose **Open**\.

**To create a new file**, do one of the following:
+ In the **Environment** window, right\-click the folder where you want the new file to go, and then choose **New File**\. Type the file's name and extension, and then press  Enter \.
+ Choose **File, New File** on the menu bar\. When you're ready to save the file, choose **File, Save** or **File, Save As** on the menu bar\. Then use the **Save As** dialog box that displays to name the file and choose where to save it\.
+ In the tab buttons bar in the editor pane, choose the **\+** button, and then choose **New File**\. When you're ready to save the file, choose **File, Save** or **File, Save As** on the menu bar\. Then use the **Save As** dialog box that displays to name the file and choose where to save it\.

    
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-env-new.png)

  

**To create a new folder**, right\-click the folder in the **Environment** window where you want the new folder to go, and then choose **New Folder**\. Type the folder's name, and then press  Enter \.

**To save a file**, with the file open and its contents visible in the editor pane, choose **File, Save** on the menu bar\.

**To rename a file or folder**, right\-click the file or folder in the **Environment** window\. Type the replacement name, and then press  Enter \.

**To delete files or folders**, choose the files or folders in the **Environment** window\. Right\-click the selection, and then choose **Delete**\. Then confirm the deletion by choosing **Yes** \(for a single selection\) or **Yes to All**\.

**To cut, copy, paste, or duplicate files or folders**, choose the files or folders in the **Environment** window\. Right\-click the selection, and then choose **Cut**, **Copy**, **Paste**, or **Duplicate**, respectively\.

**To collapse folders**, choose the gear icon in the **Environment** window, and then choose **Collapse All Folders**\. 



![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-env-collapse.png)

**To show or hide hidden files**, choose the gear icon in the **Environment** window, and then choose **Show Hidden Files**\. 

### Working with code<a name="code-editor-code"></a>

Use the editor pane in the code editor to view and write code\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-editor-pane.png)



#### Working with tab buttons<a name="code-editor-code-tab-buttons"></a>

Use the *tab buttons bar* to select, view, and create files\.



![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-tab-buttons-bar.png)



**To display an open file's contents**, do one of the following:
+ Choose the file's tab\.
+ Choose the drop\-down menu button in the tab buttons bar, and then choose the file's name\.

    
![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-drop-down-list.png)

  

**To close an open file**, do one of the following:
+ Choose the **X** icon in the file's tab\.
+ Choose the file's tab\. Then choose the drop\-down menu button in the tab buttons bar, and choose **Close Pane**\.

**To close multiple open files**, choose the drop\-down menu in the tab buttons bar, and then choose **Close All Tabs in All Panes** or **Close All But Current Tab** as needed\.

**To create a new file**, choose the **\+** button in the tab buttons bar, and then choose **New File**\. When you're ready to save the file, choose **File, Save** or **File, Save As** on the menu bar\. Then use the **Save As** dialog box that displays to name the file and choose where to save it\.

#### Working with the status bar<a name="code-editor-code-status-bar"></a>

Use the status bar to move quickly to a line in the active file and to change how code is displayed\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-status-bar.png)



**To move quickly to a line in the active file**, choose the line selector, type the line number to go to, and then press  Enter \.



![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-line-selector.png)



**To change the code color scheme in the active file**, choose the code color scheme selector, and then choose the new code color scheme\. 



![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-code-color.png)

**To change in the active file whether soft tabs or spaces are used, the tab size, or whether to convert to spaces or tabs**, choose the spaces and tabs selector, and then choose the new settings\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-spaces-tabs.png)

**To change for all files whether to show or hide invisible characters or the gutter, auto\-pair brackets or quotes, wrap lines, or the font size**, choose the gear icon, and then choose the new settings\. 

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-status-bar-settings.png)



### Working in fullscreen mode<a name="code-editor-fullscreen"></a>

You can expand the code editor to get more room to work with your code\.

To expand the code editor to the edges of the web browser window, choose the **Toggle fullscreen** button in the menu bar\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-menu-bar-fullscreen.png)



To shrink the code editor to its original size, choose the **Toggle fullscreen** button again\.

In fullscreen mode, additional options are displayed on the menu bar: **Save** and **Test**\. Choosing **Save** saves the function code\. Choosing **Test** or **Configure Events** enables you to create or edit the function's test events\. 

### Working with preferences<a name="code-editor-prefs"></a>

You can change various code editor settings such as which coding hints and warnings are displayed, code folding behaviors, code autocompletion behaviors, and much more\. 

To change code editor settings, choose the **Preferences** gear icon in the menu bar\.

![\[Image NOT FOUND\]](http://docs.aws.amazon.com/lambda/latest/dg/images/code-editor/code-editor-menu-bar-preferences.png)

For a list of what the settings do, see the following references in the *AWS Cloud9 User Guide*\.
+ [Project setting changes you can make](https://docs.aws.amazon.com/cloud9/latest/user-guide/settings-project.html#settings-project-change)
+ [User setting changes you can make](https://docs.aws.amazon.com/cloud9/latest/user-guide/settings-user.html#settings-user-change)

Note that some of the settings listed in those references are not available in the code editor\.
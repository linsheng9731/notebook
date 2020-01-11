#conding=utf8  
import os 

#  - [backend](https://github.com/linsheng9731/notebook/tree/master/backend)

ignoreList = ['out', 'bin', 'README.md', 'img', 'target']
def fileTree(dir, depth):
    if depth > 2:
        return;
    entries = os.scandir(dir)
    for entry in entries:
        name = entry.name
        if (name.endswith('.md', 3)) or ( (name not in ignoreList) and entry.is_dir() and not name.startswith('.')):
            print(render(depth, entry.name))
        if entry.is_dir() and (not name.startswith('.')):
            fileTree(entry, depth+1)

def tab(depth):
    return ' ' * depth; 

def render(depth, name):
    return tab(depth) + "- [{}](https://github.com/linsheng9731/notebook/tree/master/{})".format(name, name)
    
fileTree('./', 1)
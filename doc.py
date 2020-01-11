#conding=utf8  
import os 

ignoreList = ['out', 'bin', 'README.md', 'img', 'target']
output = []
def fileTree(dir, depth):
    if depth > 1:
        return;
    entries = os.scandir(dir)
    for entry in entries:
        name = entry.name
        if (name not in ignoreList) and (entry.is_dir() and not name.startswith('.')):
            output.append(render(depth, entry.name))
        if entry.is_dir() and (not name.startswith('.')):
            fileTree(entry, depth+1)

def tab(depth):
    return '  ' * depth; 

def render(depth, name):
    return tab(depth) + "- [{}](https://github.com/linsheng9731/notebook/tree/master/{})".format(name, name)

fileTree('./', 0)
fo = open("README.md", "w")
fo.write("# Catalog\n")
for e in output:
    fo.write(e + "\n")
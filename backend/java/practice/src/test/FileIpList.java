package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.BitSet;
import java.util.HashMap;

/**
 * 基于文件的 ip 地址黑白名单过滤功能
 * 从文件中加载 ip 列表 每行一个 ip
 *
 * @author damon lin
 * 2020/11/17
 */
public class FileIpList implements IpList {

    public HashMap<String, BitSet> invalidIpList = new HashMap<String, BitSet >();
    BitSet ipSet = new BitSet();

    public FileIpList(String path) throws IOException {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while((str = bufferedReader.readLine()) != null)
            {
                String[] segments = str.split("\\.");
                String key = segments[0];
                int ip = Integer.parseInt(segments[1] + segments[2] + segments[3]);
                BitSet ipSet = invalidIpList.get(key);
                if(ipSet == null) {
                    ipSet = new BitSet();
                }
                ipSet.set(ip);
                invalidIpList.put(key, ipSet);
            }
        } catch (IOException e) {
            // logger.error( e.getMessage());
            throw e;
        } finally {
            //close
            inputStream.close();
        }
    }

    @Override
    public boolean isInList(String str) {
        String[] segments = str.split("\\.");
        String key = segments[0];
        int ip = Integer.parseInt(segments[1] + segments[2] + segments[3]);
        BitSet ipSet = invalidIpList.get(key);
        if(ipSet != null) {
            return ipSet.get(ip);
        } else {
            return false;
        }
    }

}


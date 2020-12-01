package test;

import java.io.IOException;

/**
 * Main
 *
 * @author damon lin
 * 2020/11/17
 */
public class Main {
    public static void main(String[] args) throws IOException {
        IpList ipList = new FileIpList("test.ip");
        System.out.println(ipList.isInList("255.255.255.255"));
        System.out.println(ipList.isInList("192.168.1.1"));
        System.out.println(ipList.isInList("192.168.11.1"));
        System.out.println(ipList.isInList("192.168.1.11"));
    }
}

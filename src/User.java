
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class User implements Serializable {

    private String name;
    private String account;
    private char[] password;
    private long id;
    private int dairynum;// 日记数量
    private boolean isWelcome = true;// 是否显示欢迎页面
    //按需添加更多属性和方法

    public User() {
        // 无参数构造函数
    }

    public User(String name, String account, char[] password, long id) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.id = id;
        dairynum = 0;
    }

    public User(String name, String account, char[] password, long id, int dairynum) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.id = id;
        this.dairynum = dairynum;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public int getDairynum() {
        return dairynum;
    }

    public void setDairynum(int dairynum) {
        this.dairynum = dairynum;
    }

    public char[] getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }
}

class UserManagement {

    private Map<String, User> userDatabase;// 用户数据库
    private AtomicLong idCounter;// 用户ID计数器，多线程安全
    private String dataFile;// 用户数据文件

    //按需添加更多属性和方法  
    public UserManagement() {// 构造方法
        dataFile = "Data\\userData.ser";
        userDatabase = new HashMap<>();
        idCounter = new AtomicLong(1);
        loadUserData();
    }

    private void loadUserData() {// 加载用户数据
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {// 读取用户数据文件
            userDatabase = (Map<String, User>) ois.readObject();// 读取用户数据库
            idCounter.set((Long) ois.readObject());// 读取用户ID计数器
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("没有找到用户数据文件或读取失败：" + e.getMessage());
        }
    }

    private void saveUserData() {// 保存用户数据
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {// 写入用户数据文件
            oos.writeObject(userDatabase);// 写入用户数据库
            oos.writeObject(idCounter.get());// 写入用户ID计数器
        } catch (IOException e) {
            System.out.println("保存用户数据失败：" + e.getMessage());
        }
    }

    public boolean registerUser(String name, String account, char[] password) {// 注册用户,返回是否注册成功
        if (userDatabase.containsKey(account)) {// 如果账号已存在
            return false;// 返回注册失败
        }
        long id = idCounter.getAndIncrement();// 生成用户ID
        User newUser = new User(name, account, password, id);// 创建新用户对象
        userDatabase.put(account, newUser);// 添加到用户数据库
        saveUserData();// 保存用户数据
        return true;
    }

    public User login(String account, char[] password) {// 登录
        User user = userDatabase.get(account);// 从用户数据库获取用户
        if (user != null && String.valueOf(user.getPassword()).equals(String.valueOf(password))) {// 如果用户存在且密码正确
            return user;
        }
        return null;
    }

    public int getDiarynum(User user) {
        return user.getDairynum();
    }

    public void setDiarynum(User user, int dairynum) {
        user.setDairynum(dairynum);
        saveUserData();
    }
}

// 图
class MyMap<K, V> {

    private static class Entry<K, V> {

        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int SIZE = 16;
    private LinkedList<Entry<K, V>>[] buckets;

    public MyMap() {
        buckets = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int getBucketIndex(K key) {
        return key.hashCode() % SIZE;
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }
}

// 链表
class MyList<T> {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 10;

    public MyList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public void add(T element) {
        if (size == elements.length) {
            resize();
        }
        elements[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    private void resize() {
        int newSize = elements.length * 2;
        Object[] newElements = new Object[newSize];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
}

// 双向链表
class MyLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public void remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
            if (current.next == null) {
                tail = current;
            }
        }
        size--;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}

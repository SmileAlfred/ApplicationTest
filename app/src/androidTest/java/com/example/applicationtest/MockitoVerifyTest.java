package com.example.applicationtest;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * 2021-08-13 Unit tests for {@link MockitoVerify}.
 * Mockito 的验证行为
 */
public class MockitoVerifyTest {

    @Mock
    private LinkedList mock_LinkedList;
    @Mock
    List mock_List;


    @Before
    public void setup() {
        initMocks(this);
    }

    //1.验证行为；
    @Test
    public void verify_behaviour() {
        //Mock 接口；模拟创建一个List对象
        //使用mock的对象
        mock_List.add(1);
        mock_List.clear();

        //验证add(1)和clear()行为是否发生
        verify(mock_List).add(1);
        verify(mock_List).clear();

    }

    //2.stubbing
    @Test
    public void verify_stubbing() {
        when(mock_LinkedList.get(0)).thenReturn("first");
        when(mock_LinkedList.get(999)).thenThrow(new RuntimeException());

        //following prints "first"
        mLog("verify_stubbing", mock_LinkedList.get(0) + "");
        //following throws runtime exception
        mLog("verify_stubbing", mock_LinkedList.get(1) + "");//返回 null；如果没有指定返回的情况

        //following prints "null" because get(999) was not stubbed
        mLog("verify_stubbing", mock_LinkedList.get(999) + "");

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mock_LinkedList).get(0);//用以判断是否调用了 .get(0)
    }

    //3.参数匹配器
    @Test
    public void verify_arg() {
        //stubbing using built-in anyInt() argument matcher
        when(mock_LinkedList.get(anyInt())).thenReturn("element");

        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
        //when(mockedList.contains(argThat(isValid()))).thenReturn("element");

        //following prints "element"
        System.out.println(mock_LinkedList.get(999));

        //you can also verify using an argument matcher
        verify(mock_LinkedList).get(anyInt());
    }

    //4.验证调用次数
    @Test
    public void verifying_number_of_invocations() {
        mock_List.add(1);
        mock_List.add(2);
        mock_List.add(2);
        mock_List.add(3);
        mock_List.add(3);
        mock_List.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        verify(mock_List).add(1);
        verify(mock_List, times(1)).add(1);
        //验证是否被调用2次
        verify(mock_List, times(2)).add(2);
        //验证是否被调用3次
        verify(mock_List, times(3)).add(3);
        //验证是否从未被调用过
        verify(mock_List, never()).add(4);
        //验证至少调用一次
        verify(mock_List, atLeastOnce()).add(1);
        //验证至少调用2次
        verify(mock_List, atLeast(2)).add(2);
        //验证至多调用3次
        verify(mock_List, atMost(3)).add(3);
    }

    //5.模拟方法体抛出异常
    @Test(expected = RuntimeException.class)
    public void doThrow_when() {
        doThrow(new RuntimeException()).when(mock_List).add(1);
        mock_List.add(1);
    }

    //6.验证调用顺序
    @Test
    public void verification_in_order() {
        List list2 = mock(List.class);

        mock_List.add(1);
        list2.add("hello");
        mock_List.add(2);
        list2.add("world");
        //将需要排序的mock对象放入InOrder
        InOrder inOrder = inOrder(mock_List, list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(mock_List).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(mock_List).add(2);
        inOrder.verify(list2).add("world");
    }

    //7.确保模拟对象上无互动发生
    @Test
    public void verify_interaction() {
        List list2 = mock(List.class);
        List list3 = mock(List.class);

        mock_List.add(1);
        verify(mock_List).add(1);
        verify(mock_List, never()).add(2);
        //验证零互动行为
        verifyZeroInteractions(list2, list3);
    }

    //8.找出冗余的互动(即未被验证到的)
    @Test(expected = NoInteractionsWanted.class)
    public void find_redundant_interaction() {
        mock_List.add(1);
        mock_List.add(2);
        verify(mock_List, times(2)).add(anyInt());
        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
        verifyNoMoreInteractions(mock_List);

        List list2 = mock(List.class);
        list2.add(1);
        list2.add(2);
        verify(list2).add(1);
        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
        verifyNoMoreInteractions(list2);
    }

    //10.连续调用
    @Test(expected = RuntimeException.class)
    public void consecutive_calls() {
        //模拟连续调用返回期望值，如果分开，则只有最后一个有效
        when(mock_List.get(0)).thenReturn(0);
        when(mock_List.get(0)).thenReturn(1);
        when(mock_List.get(0)).thenReturn(2);

        when(mock_List.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
        assertEquals(2, mock_List.get(0));
        assertEquals(2, mock_List.get(0));
        assertEquals(0, mock_List.get(1));
        assertEquals(1, mock_List.get(1));
        //第三次或更多调用都会抛出异常
        mock_List.get(1);
    }

    //11.使用回调生成期望值
    @Test
    public void answer_with_callback() {
        //使用Answer来生成我们我们期望的返回
        when(mock_List.get(anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return "hello world:" + args[0];
            }
        });
        assertEquals("hello world:0", mock_List.get(0));
        assertEquals("hello world:999", mock_List.get(999));
    }

    //12.监控真实对象;使用spy来监控真实的对象，需要注意的是此时我们需要谨慎的使用when-then语句，而改用do-when语句
    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects() {
        List list = new LinkedList();
        List spy = spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        //when(spy.get(0)).thenReturn(3);

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        doReturn(999).when(spy).get(999);
        //预设size()期望值
        when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        assertEquals(100, spy.size());
        assertEquals(1, spy.get(0));
        assertEquals(2, spy.get(1));
        verify(spy).add(1);
        verify(spy).add(2);
        assertEquals(999, spy.get(999));
        assertEquals(null, spy.get(98989));//当没有指定预期值时，用任何值进行判定都是 true！
    }

    //13.修改对未预设的调用返回默认期望值
    @Test
    public void unstubbed_invocations() {
        //mock对象使用Answer来对未预设的调用返回默认期望值
        List mock = mock(List.class, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 999;
            }
        });
        //下面的get(1)没有预设，通常情况下会返回NULL，但是使用了Answer改变了默认期望值
        assertEquals(999, mock.get(1));
        //下面的size()没有预设，通常情况下会返回0，但是使用了Answer改变了默认期望值
        assertEquals(999, mock.size());
    }

    //14.捕获参数来进一步断言
    @Test
    public void capturing_args() {
        PersonDao personDao = mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        personService.update(1, "jack");
        verify(personDao).update(argument.capture());
        assertEquals(1, argument.getValue().getId());
        assertEquals("jack", argument.getValue().getName());
    }

    class Person {
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao {
        public void update(Person person);
    }

    class PersonService {
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id, String name) {
            personDao.update(new Person(id, name));
        }
    }

    //15.真实的部分mock
    @Test
    public void real_partial_mock() {
        //通过spy来调用真实的api
        List list = spy(new ArrayList());
        assertEquals(0, list.size());
        A a = mock(A.class);
        //通过thenCallRealMethod来调用真实的api
        when(a.doSomething(anyInt())).thenCallRealMethod();
        assertEquals(999, a.doSomething(999));
    }

    class A {

        public int doSomething(int i) {
            return i;
        }
    }

    //16.重置mock
    @Test
    public void reset_mock() {
        when(mock_List.size()).thenReturn(10);
        mock_List.add(1);
        assertEquals(10, mock_List.size());
        //重置mock，清除所有的互动和预设
        reset(mock_List);
        assertEquals(0, mock_List.size());
    }


    public void mLog(String methodName, String msg) {
        Log.e("\tTAG\t", methodName + "\t" + msg);
    }

    public void mSout(String methodName, String msg) {
        System.out.println("\tTAG\t" + methodName + "\t" + msg);
    }

}

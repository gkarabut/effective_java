import java.util.concurrent.Exchanger;

/**
 * Created by mnikonova on 02.12.15.
 */
public class ExchangerDemo {
    public static void main (String[] args){
        Exchanger<String> exchanger = new Exchanger();
        /*should start from consumer*/
        new StringConsumer(exchanger);
        new StringConsumer(exchanger);
        new ThirdPartyProducer(exchanger);
        new ThirdPartyProducer(exchanger);
        new StringProducer(exchanger);
        new StringConsumer(exchanger);
        new StringConsumer(exchanger);

    }


    public static class StringConsumer implements Runnable{
        private Exchanger exchanger;
        private String str;

        public StringConsumer(Exchanger ex){
            this.exchanger = ex;
            this.str = new String();
            new Thread(this).start();
        }

        public void run(){
            for (int i=0; i<3; i++)
                try{
                    str = exchanger.exchange(str).toString();

                }catch (Exception ex){
                    ex.printStackTrace();
                }
        }
    }

    public static class StringProducer implements Runnable{
        private Exchanger exchanger;
        private String str;

        public StringProducer(Exchanger ex){
            str = new String();
            exchanger = ex;
            new Thread(this).start();
        }

        public void run(){
            for (int i=0; i<3; i++)
                str+="a"+i;
            try{
                System.out.println("Print buffer of string producer " + str);
                str = exchanger.exchange(str).toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static class ThirdPartyProducer implements Runnable{
        private Exchanger exchanger;
        private String str;

        public ThirdPartyProducer(Exchanger ex){
            str = new String();
            exchanger = ex;
            new Thread(this).start();
        }

        public void run(){
            for (int i=0; i<3; i++)
                str+="H"+i;
            try{
                System.out.println("Print buffer of third party string producer " + str);
                str = exchanger.exchange(str).toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }
}

package CustomClasses;

import java.time.LocalTime;

public class IOCheck {




    public  static  String StringNullCheck(String str, String SingleMsg)
    {
       String Msg = "";
        if(str == null || str == " ")
        {
            Msg = SingleMsg +" - You Didn't Choose " + SingleMsg + "\n";
        }
        return Msg;

    }

    public  static  String IntegerNullCheck(Integer num, String SingleMsg)
    {
        String Msg = "";
        if(num == null)
        {
            Msg = SingleMsg + " - You Didn't Choose " + SingleMsg + "\n";
        }
        return Msg;

    }

    public  static  String TimeNullCheck(LocalTime num, String SingleMsg)
    {
        String Msg = "";
        if(num == null)
        {
            Msg = SingleMsg + " - You Didn't Choose " + SingleMsg + "\n";
        }
        return Msg;

    }

    public  static  String BooleanNullCheck(Boolean num, String SingleMsg)
    {
        String Msg = "";
        if(num == null)
        {
            Msg = SingleMsg + " - You Didn't Choose " + SingleMsg + "\n";
        }
        return Msg;

    }



    public static String CheckName(String name, String Msg1) {

       String Msg = "";

        for (char SingleChar : name.toCharArray()) {
            /** Numbers check */
            if (Character.isDigit(SingleChar)) {
                Msg =  Msg1 + " - Please Type only English letters  \n ";
                break;
            }
            /** English letters check check */
            if (!EnglishLetterCheck(SingleChar) && (SingleChar != ' ')) {
                Msg = Msg1 + " - Please Type only English letters \n";
                break;
            }
        }

        if(name.equals(""))
        {
            Msg =  Msg1 + " - You didn't enter " + Msg1 +  "\n";
        }
       return  Msg;
    }


    public static boolean stringIsAName(String name) {

        boolean ReturnValue = true;
        for (char SingleChar : name.toCharArray()) {
            /** Numbers check */
            if (Character.isDigit(SingleChar)) {
                System.out.println("Invalid Input, you Typed Numbers , Please Type only letters ");
                ReturnValue = false;
                break;
            }
            /** English letters check check */
            if (!EnglishLetterCheck(SingleChar) && (SingleChar != ' ')) {
                System.out.println("Invalid Input, Please Type only English letters");
                ReturnValue = false;
                break;
            }
        }

        if(ReturnValue == true) {
            if(LengthNameCheck(name))
            {
                ReturnValue = false;
            }
            else {
                if (!SpaceCheck(name)) {
                    ReturnValue = false;
                }
            }
        }
        return ReturnValue;
    }


    private static boolean EnglishLetterCheck(Character c)
    {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean SpaceCheck(String name) {

        boolean ReturnValue = true;
        int spaces = (int) name.chars()
                .filter(c -> c == (int) ' ')
                .count();
        if (spaces < 1) {
            System.out.println("Invalid Input, You didn't enter last name");
            ReturnValue = false;
        }
        return ReturnValue;
    }

    private static boolean LengthNameCheck(String name)
    {
       boolean ReturnValue = false;
        if(name.length() > 35)
        {
            System.out.println("The name is too long, please enter a name that is less then 35 letters");
            ReturnValue = true;
        }
        return  ReturnValue;
    }

    public static boolean HoursCheck(int hour)
    {
        boolean ReturnValue = true;
        if(!((hour > 0 && hour < 24) || hour == 0))
        {
            System.out.println("Please enter a number between 0 - 23");
            ReturnValue = false;
        }
        return  ReturnValue;
    }

    public static boolean MinutesCheck(int minutes)
    {
        boolean ReturnValue = true;
        if(!((minutes >= 0 && minutes <= 59) || minutes == 00))
        {
            System.out.println("Please enter a number between 00 - 59");
            ReturnValue = false;
        }
        return  ReturnValue;
    }

    public static boolean ppkCheck(int ppk)
    {
        boolean ReturnValue = true;
        if(ppk < 0)
        {
            System.out.println("Please enter positive number");
            ReturnValue = false;
        }
        return  ReturnValue;
    }

    public static boolean CapacityCheck(int Capacity)
    {
        boolean ReturnValue = true;
        if(Capacity < 0)
        {
            System.out.println("Please enter positive number");
            ReturnValue = false;
        }

        return  ReturnValue;
    }

}

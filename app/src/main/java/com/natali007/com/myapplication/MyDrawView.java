package com.natali007.com.myapplication;

import java.util.ArrayList;

import android.util.DisplayMetrics;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Display;
import android.view.WindowManager;


/**
 * Created by Натали on 30.11.2015.
 */
public class MyDrawView extends View {

    //The style and color information for the coordinate plane
    private Paint p;
    //The style and color information for the lines on coordinate plane
    private Paint paintForLines;
    //The style and color information for Points and graph of the function
    private Paint paintPoint;
    //Path for drawing a graph of the function
    private Path path;
    //Coordinates set during the touch
    private float TouchX, TouchY;
    //The height and width of the frame
    private int frameHeight, frameWidth;
    //An array that stores the values of the set points (coordinates of the phone)
    public static ArrayList<Points> arrayPoints;
    //An array that stores the values of the set points (real coordinates)
    public static ArrayList<Points> realPoints;
    //An array that stores the values of the set points after Lagrange polynomial
    public static ArrayList<Points> arrayNewPoints;
    //Draw a graph of the function
    private boolean toPlot = false;
    //length between Yside and horizontal side of frame , and length between Xside and vertical side of frame
    private int lengthToXside, lengthToYside;
    // The radius of the point
    private int circleR;
    //if points is drawn by
    private boolean pointOne = false;
    private boolean pointTwo = false;
    private boolean pointThree = false;
    private boolean pointFour = false;
    private boolean pointFive = false;
    //if points is move
    private boolean movePoint = false;
    //If the graph of function is built
    private boolean functionIsDraw = false;

    private Points onTouchPoint;
    private int distanceBetweenPoints;

    public MyDrawView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public MyDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public MyDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init(){

        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(2);

        paintPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPoint.setStrokeWidth(2);
        paintForLines = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForLines.setStrokeWidth(1);
        paintForLines.setStyle(Paint.Style.STROKE);
        paintForLines.setColor(Color.GRAY);

        path = new Path();

        arrayPoints = new ArrayList<Points>();
        arrayNewPoints = new ArrayList<Points>();
        realPoints = new ArrayList<Points>();

        frameHeight = (int) (getResources().getDimension(R.dimen.l_height) / getResources().getDisplayMetrics().density);
        frameWidth =  (int) (getResources().getDimension(R.dimen.l_width) / getResources().getDisplayMetrics().density);
        circleR = (int) (getResources().getDimension(R.dimen.radius) / getResources().getDisplayMetrics().density);

        lengthToXside= 20;
        lengthToYside= 10;

        distanceBetweenPoints = (frameWidth - 20)/6;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Set the canvas color
        canvas.drawARGB(80, 102, 204, 255);

        // The coordinate axis Y
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK); //10, 880-870(10), 10, 880-20 (860)
        canvas.drawLine(lengthToYside,  10, lengthToYside ,  frameHeight-lengthToXside, p);
        //The coordinate axis X / 10, 880-20(860), 520, 880-20 (860)
        canvas.drawLine(lengthToYside, frameHeight-lengthToXside, frameWidth - 10 , frameHeight-lengthToXside, p);
       // canvas.drawLine(lengthToYside,  frameHeight-lengthToXside, lengthAxisX ,  frameHeight-lengthToXside, p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.GRAY);
        p.setStrokeWidth(2);
        int number = 1; // numbers on axis X
        //Drawing points on the  coordinate axis X
        for (int i=(lengthToYside+distanceBetweenPoints); i<=(frameWidth - 10); i+=distanceBetweenPoints){
            canvas.drawCircle(i,  frameHeight-lengthToXside, 2, p);
            canvas.drawText(String.valueOf(number), i, frameHeight-2, paintForLines);
            number++;
            canvas.drawLine(i, frameHeight-lengthToXside, i ,  10, paintForLines);

        }
        //Drawing points on the  coordinate axis Y
        for (int i=(lengthToXside+distanceBetweenPoints); i<=(frameHeight - 10); i+=distanceBetweenPoints){
            p.setStrokeWidth(2);
            canvas.drawCircle(lengthToYside,  frameHeight-i, 2, p);
            canvas.drawLine(lengthToYside,  frameHeight-i, frameWidth - 10  ,frameHeight-i, paintForLines);
        }


        //Draw the points - values of the function
        paintPoint.setColor(Color.MAGENTA);
        paintPoint.setStyle(Paint.Style.FILL);
        for (int i=0; i < arrayPoints.size(); i++ ){
            Points onTouch = arrayPoints.get(i);
            canvas.drawCircle( onTouch.x, onTouch.y, circleR, paintPoint);
        }

        // If clicked the button for draw a graph of the function
        if (toPlot){
            Points onNewPoints;
            path.reset();
            for (int i=0; i < arrayNewPoints.size()-1; i++){
                onNewPoints = arrayNewPoints.get(i);
                if ((onNewPoints.y<=(frameHeight - lengthToXside)) && (onNewPoints.y>= 10)){
                    path.moveTo(onNewPoints.x, onNewPoints.y);
                    path.lineTo(arrayNewPoints.get(i+1).x, arrayNewPoints.get(i+1).y );
                }

            }
            paintPoint.setStyle(Paint.Style.STROKE);
            paintPoint.setColor(Color.YELLOW);
            canvas.drawPath(path, paintPoint);
            toPlot = false;
        }
    }
    @Override
    public  boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            TouchX = (float) event.getX();
            TouchY = (float) event.getY();
            //If a touch in the coordinate plane
            if ((functionIsDraw == false) && (TouchY >= 10) && (TouchY <= frameHeight - lengthToXside)) {

                if ((TouchX >= (lengthToYside + distanceBetweenPoints) - 15) && (TouchX < (lengthToYside + distanceBetweenPoints) + 15) && pointOne == false) {
                    arrayPoints.add(new Points(lengthToYside + distanceBetweenPoints, TouchY));
                    pointOne = true;
                    invalidate();
                }

                if ((TouchX >= (lengthToYside + distanceBetweenPoints * 2) - 15) && (TouchX < (lengthToYside + distanceBetweenPoints * 2) + 15) && pointTwo == false) {
                    arrayPoints.add(new Points(lengthToYside + distanceBetweenPoints * 2, TouchY));
                    pointTwo = true;
                    invalidate();
                }

                if ((TouchX >= (lengthToYside + distanceBetweenPoints * 3) - 15) && (TouchX < (lengthToYside + distanceBetweenPoints * 3) + 15) && pointThree == false) {
                    arrayPoints.add(new Points(lengthToYside + distanceBetweenPoints * 3, TouchY));
                    pointThree = true;
                    invalidate();
                }

                if ((TouchX >= (lengthToYside + distanceBetweenPoints * 4) - 15) && (TouchX < (lengthToYside + distanceBetweenPoints * 4) + 15) && pointFour == false) {
                    arrayPoints.add(new Points(lengthToYside + distanceBetweenPoints * 4, TouchY));
                    pointFour = true;
                    invalidate();
                }

                if ((TouchX >= (lengthToYside + distanceBetweenPoints * 5) - 15) && (TouchX < (lengthToYside + distanceBetweenPoints * 5) + 15) && pointFive == false) {
                    arrayPoints.add(new Points(lengthToYside + distanceBetweenPoints * 5, TouchY));
                    pointFive = true;
                    invalidate();
                }

                else {
                        for (int i = 0; i < arrayPoints.size(); i++) {
                            onTouchPoint = arrayPoints.get(i);
                            if ((TouchX <= onTouchPoint.x + 8) && (TouchX >= onTouchPoint.x - 8) && (TouchY <= onTouchPoint.y + 8) && (TouchY >= onTouchPoint.y - 8)) {
                                movePoint = true;
                                break;
                            }
                        }
                }
            }
        }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchX = (float) event.getX();
                TouchY = (float) event.getY();
                if ( (movePoint == true) && (TouchY >= 10) && (TouchY <= frameHeight - lengthToXside)) {
                    onTouchPoint.y = TouchY;
                    invalidate();
                }
            }
        if (event.getAction() == MotionEvent.ACTION_UP){
            movePoint = false;
        }
        return true;
    }

    public void onClicklLagrange (){
        toPlot = true;
        float newX, newY, lagrangeY;
        for (int i=0; i< arrayPoints.size(); i++){
            Points touchPoints = arrayPoints.get(i);
            // Change  the phone coordinates  to real coordinates
            realPoints.add(new Points((touchPoints.x - lengthToYside)/distanceBetweenPoints, ((frameHeight - touchPoints.y) -lengthToXside)/distanceBetweenPoints));
        }
        for (float i = (float) 0.1; i<=6; i+=0.1){
            //We get real coordinates and change the phone coordinates. newX and newY it's the phone coordinates
            newX = i*distanceBetweenPoints + lengthToYside;
            lagrangeY = lagrangeFunction(i);
            newY = ((float)frameHeight - lengthToXside) -  lagrangeY*distanceBetweenPoints;
            arrayNewPoints.add(new Points(newX , newY));
        }
        invalidate();
    }

    public float lagrangeFunction(float x) {
        float S = 0;
        float d1, d2;
        for (int i=0; i<realPoints.size(); i++){
            Points TouchPointI = realPoints.get(i);
            d1=1;
            d2=1;
            for (int j=0; j<realPoints.size(); j++){
                if (i!=j){
                    Points TouchPointJ = realPoints.get(j);
                    d1 = d1*(x - TouchPointJ.x);
                    d2 = d2*(TouchPointI.x - TouchPointJ.x);
                }
            }
            S += TouchPointI.y * (d1/d2);
        }
        functionIsDraw = true;
        return S;
    }

    public void onClicklClear(){
        arrayPoints.clear();
        arrayNewPoints.clear();
        realPoints.clear();
        invalidate();
        pointOne = false;
        pointTwo = false;
        pointThree = false;
        pointFour = false;
        pointFive = false;
        functionIsDraw = false;
    }

}

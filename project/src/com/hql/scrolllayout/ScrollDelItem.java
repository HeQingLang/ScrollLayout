package com.hql.scrolllayout;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollDelItem extends LinearLayout {
	
	//按下触点
	private int m_downX = 0;
	private int m_downY = 0;
	//当前触点
	private int m_curX = 0;
	private int m_curY = 0;
	
	private int m_offsetX= 0; //水平位移
	
	private float m_leftPercent = 0.0f;//左边  向右最大滑动占整view比重
	private float m_rightPercent = 0.0f;//右边  向左最大滑动占整view比重
	
	
	//初始View大小
	private int m_initW = 0;
	private int m_initH = 0;
	
	private Scroller m_scroller;
	private Context  m_context;
	private LayoutInflater m_lf;
	private LinearLayout m_bodyLayout;
	private LinearLayout m_headLayout;
	private LinearLayout m_endLayout;
	
	public ScrollDelItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_context = context;
		m_scroller = new Scroller(m_context);
		m_lf = LayoutInflater.from(m_context);
	}

	public ScrollDelItem(Context context) {
		this(context,null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if ( widthMeasureSpec > 0 && m_initW == 0) {
			flushLayout();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void computeScroll() {
		if (m_scroller.computeScrollOffset()) {
			scrollTo( m_scroller.getFinalX(),m_scroller.getFinalY() );
			postInvalidate();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			m_downX = (int)event.getX();
			m_downY = (int)event.getY();
			m_curX = m_downX;
			m_curY = m_downY;
			
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getX() > m_downX ) {
				//向右滑
			}
			else {
				//向左滑
				if (m_endLayout != null) {
					if (m_offsetX +  m_curX - (int)event.getX() < m_initW*m_leftPercent) {
						m_offsetX = m_offsetX + m_curX - (int)event.getX();
						m_curX =(int)event.getX();
						m_curY =(int)event.getY();
						smoothScrollTo( m_offsetX, 0);
					}
					
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
	
			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public void setBodyLayout(int resid){
		View view =  m_lf.inflate(resid, null);
		if (view != null) {
			setBodyLayout((LinearLayout)view);
		}
		 
	}
	
	private void smoothScrollTo(int x,int y){
		m_scroller.startScroll(0, 0, x, y);
		invalidate();
	}
	public void setBodyLayout(LinearLayout layout){
		if (layout!= null) {
			if(m_bodyLayout!=null){
				ViewParent vp = m_bodyLayout.getParent();
				if (vp != null) {
					((ViewGroup)vp).removeView(m_bodyLayout);
					m_bodyLayout = null;
				}
				
			} 
			m_bodyLayout = layout;
		}
	}
	
	public void setHeadLayout(int resid){
		View view =  m_lf.inflate(resid, null);
		if (view != null) {
			setHeadLayout((LinearLayout)view);
		}
		 
	}
	public void setHeadLayout(LinearLayout layout){
		if (layout!= null) {
			if(m_headLayout!=null){
				ViewParent vp = m_headLayout.getParent();
				if (vp != null) {
					((ViewGroup)vp).removeView(m_headLayout);
					m_headLayout = null;
				}
				
			} 
			m_headLayout = layout;
		}
	}
	
	
	public void setEndLayout(int resid){
		View view =  m_lf.inflate(resid, null);
		if (view != null) {
			setEndLayout((LinearLayout)view);
		}
		 
	}
	public void setEndLayout(LinearLayout layout){
		if (layout!= null) {
			if(m_endLayout!=null){
				ViewParent vp = m_endLayout.getParent();
				if (vp != null) {
					((ViewGroup)vp).removeView(m_endLayout);
					m_endLayout = null;
				}
				
			} 
			m_endLayout = layout;
		}
	}
	
	/**
	 * 刷新View 在这里遇到一个问题，在view测量前无法获取view 的真实大小。因此等他测量后，能获取到真实大小，再把iew添加进去  
	 */
	private void flushLayout(){
		Log.i( "========>>>>>flushLayout", ""+m_initW);
		if (m_initW<= 0 || m_initH<= 0 ) {
			m_initW = this.getMeasuredWidth();
			m_initH = this.getMeasuredHeight();
		}
		ViewGroup.LayoutParams lp = this.getLayoutParams();
		if (lp == null ) {
			lp = new ViewGroup.LayoutParams(m_initW,m_initH);
		}
		if (m_bodyLayout!=null ) {
			this.addView(m_bodyLayout ,new LinearLayout.LayoutParams(m_initW,m_initH) );
		}
		if (m_endLayout!=null) {
			lp.width = m_initW + (int)(m_initW*m_rightPercent);
			lp.height = m_initH;
			this.setLayoutParams(lp);
			this.addView(m_endLayout ,new LinearLayout.LayoutParams( (int)(m_initW*m_rightPercent),m_initH) );
		}
		invalidate();
	}
}

package de.tum.kickercoding.tournamentviewer.setup.monsterdyp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import de.tum.kickercoding.tournamentviewer.tournament.TournamentGamesFragment;
import de.tum.kickercoding.tournamentviewer.tournament.TournamentStatsFragment;

public class TournamentPagerAdapter extends FragmentPagerAdapter {

	private FragmentManager fragmentManager;

	private ViewPager viewPager;

	private final static int NUM_PAGES = 2;

	public final static int PAGE_STATS = 0;
	public final static int PAGE_GAMES = 1;

	public final static String PAGER_HEADER_STATS = "Stats";
	public final static String PAGER_HEADER_GAMES = "Games";

	public TournamentPagerAdapter(FragmentManager fm, ViewPager viewPager) {
		super(fm);
		this.viewPager = viewPager;
		this.fragmentManager = fm;
	}

	@Override
	public int getCount() {
		return NUM_PAGES;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case PAGE_GAMES: {
				return new TournamentGamesFragment();
			}
			case PAGE_STATS:
			default: {
				return new TournamentStatsFragment();
			}
		}
	}

	@Override
	public int getItemPosition(final Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case PAGE_GAMES: {
				return PAGER_HEADER_GAMES;
			}
			case PAGE_STATS:
			default: {
				return PAGER_HEADER_STATS;
			}
		}
	}


	// This method shadows the private method of FragmentPagerAdapter; this approach is necssary to allow accessing
	// the fragments managed by the PagerAdapter from the activity

	/**
	 * @param containerViewId the ViewPager this adapter is being supplied to
	 * @param id              pass in getItemId(position) as this is whats used internally in this class
	 * @return the tag used for this pages fragment
	 */
	public static String makeFragmentName(int containerViewId, long id) {
		return "android:switcher:" + containerViewId + ":" + id;
	}

	/**
	 * @return may return null if the fragment has not been instantiated yet for that position - this depends on if
	 * the fragment has been viewed yet OR is a sibling covered by
	 * {@link android.support.v4.view.ViewPager#setOffscreenPageLimit(int)}. Can use
	 * this to call methods on the current positions fragment.
	 */
	@Nullable
	public Fragment getFragmentForPosition(int position) {
		String tag = makeFragmentName(viewPager.getId(), getItemId(position));
		Fragment fragment = fragmentManager.findFragmentByTag(tag);
		return fragment;
	}
}

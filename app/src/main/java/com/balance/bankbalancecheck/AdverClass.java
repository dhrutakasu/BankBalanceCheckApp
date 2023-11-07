package com.balance.bankbalancecheck;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.ShowAdsModel;
import com.facebook.ads.Ad;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdverClass {

    public static InterstitialAd interAd;
    public static setAdListerner setAdListerner;
    public static NativeAd natAd;
    private static int count = 1;

    public static void showFbookBanner(Context con, com.facebook.ads.AdSize adSize, RelativeLayout layout, String Fbid, String shows, String bAd) {
        if (!Fbid.equalsIgnoreCase("")) {
            com.facebook.ads.AdView adView = new com.facebook.ads.AdView(con, Fbid, adSize);
            layout.addView(adView);
            com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                @Override
                public void onError(Ad ad, com.facebook.ads.AdError error) {
                    if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                        ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.GONE);
                    }
                    showGAdMobBanner(con, com.google.android.gms.ads.AdSize.BANNER, layout, bAd, shows, Fbid);
                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            };
            adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
            if (shows.equalsIgnoreCase("t")) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.INVISIBLE);
            }
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    public static void showGAdMobBanner(Context con, AdSize adSize, RelativeLayout layout, String Gid, String shows, String fbookbad) {
        if (!Gid.equalsIgnoreCase("")) {
            AdView adView = new AdView(con);
            adView.setAdSize(adSize);
            adView.setAdUnitId(Gid);

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                }

                @Override
                public void onAdClosed() {
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                        ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.VISIBLE);
                    }
                    Initialize(con);
                    showFbookBanner(con, com.facebook.ads.AdSize.BANNER_HEIGHT_50, layout, fbookbad, shows, Gid);
                }

                @Override
                public void onAdImpression() {
                }

                @Override
                public void onAdLoaded() {
                }

                @Override
                public void onAdOpened() {
                }
            });
            adView.loadAd(new com.google.android.gms.ads.AdRequest.Builder().build());
            layout.addView(adView);
            if (shows.equalsIgnoreCase("t")) {
                layout.setVisibility(View.VISIBLE);
            } else {
                layout.setVisibility(View.INVISIBLE);
            }
        } else {
            layout.setVisibility(View.INVISIBLE);
        }
    }

    public static void ShowLayoutBannerAds(Context con, ProgressBar progressBar, RelativeLayout layout) {
        if (IsInternetOn(con)) {
            progressBar.setVisibility(View.VISIBLE);
            BankConstantsData.LoadAdsData(con, new BankConstantsData.LoadAdsId() {
                @Override
                public void getAdsIds(ShowAdsModel showAdsModel) {
                    progressBar.setVisibility(View.GONE);
                    if (showAdsModel.getBtype().equalsIgnoreCase("1")) {
                        if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                            ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.GONE);
                        }
                        showGAdMobBanner(con, com.google.android.gms.ads.AdSize.BANNER, layout, showAdsModel.getBad(), showAdsModel.getLogin(), showAdsModel.getFbad());
                    } else {
                        if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                            ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.VISIBLE);
                        }
                        Initialize(con);
                        showFbookBanner(con, com.facebook.ads.AdSize.BANNER_HEIGHT_50, layout, showAdsModel.getFbad(), showAdsModel.getLogin(), showAdsModel.getBad());
                    }
                }
            });
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    public interface setAdListerner {
        void AdListen();

    }

    public static void ShowGAdMobInterstitialAd(Activity activity, String Gid, String shows, String fbookiad, setAdListerner listerner) {
        if (!Gid.equalsIgnoreCase("")) {
            MobileAds.initialize(activity, initializationStatus -> {
            });

            interAd.load(activity, Gid, new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
                public void onAdLoaded(InterstitialAd ad) {
                    interAd = ad;
                    if (shows.equalsIgnoreCase("t")) {
                        if (ad != null) {
                            ad.show(activity);
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    interAd = null;
                                    AdverClass.setAdListerner = listerner;
                                    if (AdverClass.setAdListerner != null) {
                                        AdverClass.setAdListerner.AdListen();
                                        AdverClass.setAdListerner = null;
                                    }
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    interAd = null;
                                }
                            });
                            return;
                        }
                    }

                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    if (((Activity) activity).findViewById(R.id.NativeBAdContainer) != null) {
                        ((Activity) activity).findViewById(R.id.NativeBAdContainer).setVisibility(View.VISIBLE);
                    }
                    Initialize(activity);
                    ShowFbookInterstitialAd((Activity) activity, fbookiad, Gid, shows, listerner);

                    interAd = null;
                }
            });
        }
    }

    public static void ShowFbookInterstitialAd(Activity acti, String Fbid, String GId, String shows, setAdListerner listerner) {
        if (!Fbid.equalsIgnoreCase("")) {
            MobileAds.initialize(acti, new OnInitializationCompleteListener() {

                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(acti, Fbid);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    setAdListerner = listerner;
                    if (setAdListerner != null) {
                        setAdListerner.AdListen();
                        setAdListerner = null;
                    }
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    if (((Activity) acti).findViewById(R.id.NativeBAdContainer) != null) {
                        ((Activity) acti).findViewById(R.id.NativeBAdContainer).setVisibility(View.GONE);
                    }
                    ShowGAdMobInterstitialAd((Activity) acti, GId, shows, Fbid, listerner);

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (shows.equalsIgnoreCase("t")) {
                        if (interstitialAd != null) {
                            interstitialAd.show();
                        }
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };

            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build());
        }

    }

    public static void ShowLayoutInterstitialAd(Context con, setAdListerner adListerner) {
        int c = count++;
        if (IsInternetOn(con)) {
            BankConstantsData.LoadAdsData(con, new BankConstantsData.LoadAdsId() {
                @Override
                public void getAdsIds(ShowAdsModel model) {
                    if (c % Integer.parseInt(model.getAdscount()) == 0) {
                        if (model.getItype().equalsIgnoreCase("1")) {
                            if (!model.getIad().equalsIgnoreCase("")) {
                                if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                                    ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.GONE);
                                }
                                ShowGAdMobInterstitialAd((Activity) con, model.getIad(), model.getLogin(), model.getFiad(), adListerner);
                            } else {
                                if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                                    ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.VISIBLE);
                                }
                                Initialize(con);
                                ShowFbookInterstitialAd((Activity) con, model.getFiad(), model.getIad(), model.getLogin(), adListerner);
                            }
                        } else {
                            if (!model.getFiad().equalsIgnoreCase("")) {
                                if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                                    ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.VISIBLE);
                                }
                                Initialize(con);
                                ShowFbookInterstitialAd((Activity) con, model.getFiad(), model.getIad(), model.getLogin(), adListerner);
                            } else {
                                if (((Activity) con).findViewById(R.id.NativeBAdContainer) != null) {
                                    ((Activity) con).findViewById(R.id.NativeBAdContainer).setVisibility(View.GONE);
                                }
                                ShowGAdMobInterstitialAd((Activity) con, model.getIad(), model.getLogin(), model.getFiad(), adListerner);
                            }
                        }
                    } else {
                        adListerner.AdListen();
                    }
                }
            });
        } else {
            adListerner.AdListen();
        }
    }

    public static boolean IsInternetOn(Context con) {
        ConnectivityManager service = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (service.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || service.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING || service.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING || service.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        if (service.getNetworkInfo(0).getState() != NetworkInfo.State.DISCONNECTED) {
            service.getNetworkInfo(1).getState();
            NetworkInfo.State state = NetworkInfo.State.DISCONNECTED;
        }
        return false;
    }

    public static void showGAdMobNativeBanner(Activity act, String Gid, RelativeLayout layout, String shows, String adsButtonTextColor, String adsButtonColor, String adsBackgroundcolor, String fBooknad) {
        if (!Gid.equalsIgnoreCase("")) {
            AdLoader build = new AdLoader.Builder(act, Gid)
                    .forNativeAd(nativeAd -> {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (nativeAd != null) {
                                    NativeAdView nativeAdView = (NativeAdView) act.getLayoutInflater().inflate(R.layout.layout_google_mob_native_banner, (ViewGroup) null);
                                    LayoutNativeBannerAdView(nativeAd, nativeAdView, adsButtonColor, adsButtonTextColor, adsBackgroundcolor);
                                    layout.removeAllViews();

                                    if (shows.equalsIgnoreCase("t")) {
                                        layout.setVisibility(View.VISIBLE);
                                        layout.addView(nativeAdView);
                                    } else {
                                        layout.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }, 1000);
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            showFbookNativeBanner((Activity) act, ((NativeAdLayout) ((Activity) act).findViewById(R.id.NativeBAdContainer)), fBooknad, layout, shows, adsButtonTextColor, adsButtonColor, adsBackgroundcolor, Gid);
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder().build())
                    .build();
            build.loadAd(new AdManagerAdRequest.Builder().build());
        }
    }

    @SuppressLint("WrongConstant")
    private static void LayoutNativeBannerAdView(NativeAd ad, NativeAdView adView, String adsButtonColor, String adsButtonTextColor, String adsBackgroundcolor) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.MediaGAdMedia));
        adView.setHeadlineView(adView.findViewById(R.id.TvGAdHeadline));
        adView.setBodyView(adView.findViewById(R.id.TvGAdBody));
        Button button = adView.findViewById(R.id.BtnGAdCallToAction);
        button.setSelected(true);
        adView.setCallToActionView(button);
        adView.setIconView(adView.findViewById(R.id.IvGAdIcon));
        adView.setPriceView(adView.findViewById(R.id.TvGAdPrice));
        adView.setStarRatingView(adView.findViewById(R.id.RbGAdStars));
        adView.setStoreView(adView.findViewById(R.id.TvGAdStore));
        adView.setAdvertiserView(adView.findViewById(R.id.TvGAdAdvertiser));
        button.setBackgroundColor(Color.parseColor(adsButtonColor));
        button.setTextColor(Color.parseColor(adsButtonTextColor));
        ((CardView) adView.findViewById(R.id.CardGooNative)).setCardBackgroundColor(Color.parseColor(adsBackgroundcolor));
        ((TextView) adView.findViewById(R.id.TvGAdBody)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.findViewById(R.id.TvGAdAdvertiser)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.findViewById(R.id.TvGAdHeadline)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.findViewById(R.id.TvGAdBody)).setSelected(true);
        ((TextView) adView.findViewById(R.id.TvGAdAdvertiser)).setSelected(true);
        ((TextView) adView.findViewById(R.id.TvGAdHeadline)).setSelected(true);
        ((TextView) adView.findViewById(R.id.TvGAdPrice)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.findViewById(R.id.TvGAdStore)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.findViewById(R.id.TvGooAd)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) adView.getHeadlineView()).setText(ad.getHeadline());
        if (ad.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(ad.getBody());
        }
        if (ad.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(ad.getCallToAction());
        }
        if (ad.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(ad.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (ad.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(ad.getPrice());
        }
        if (ad.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(ad.getStore());
        }
        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (ad.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(ad.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(ad);
    }

    public static void showFbookNativeBanner(Activity act, NativeAdLayout layout, String Fbid, RelativeLayout relativeLayout, String shows, String adsButtonTextColor, String adsButtonColor, String adsBckgroundcolor, String nativead) {
        if (!Fbid.equalsIgnoreCase("")) {
            NativeBannerAd bannerAd = new NativeBannerAd(act, Fbid);
            NativeAdListener nativeAdListener = new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    showGAdMobNativeBanner((Activity) act, nativead, relativeLayout, shows, adsButtonTextColor, adsButtonColor, adsBckgroundcolor, Fbid);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (bannerAd == null || bannerAd != ad) {
                        return;
                    }
                    FbookNativeBannerAd(act, layout, bannerAd, adsButtonTextColor, adsButtonColor, adsBckgroundcolor);
                    relativeLayout.removeAllViews();
                    if (shows.equalsIgnoreCase("t")) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        relativeLayout.addView(layout);
                    } else {
                        relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            bannerAd.loadAd(bannerAd.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build());
        }
    }

    private static void FbookNativeBannerAd(Activity act, NativeAdLayout adLayout, NativeBannerAd bannerAd, String adsButtonTextColor, String adsButtonColor, String adsBackgroundcolor) {
        if (adLayout != null) {
            bannerAd.unregisterView();
            LayoutInflater layoutInflater = LayoutInflater.from(act);
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_facebook_native_banner_ad, adLayout, false);
            adLayout.addView(linearLayout);

            RelativeLayout adChoicesContainer = linearLayout.findViewById(R.id.RlFbAdChoicesContainer);
            AdOptionsView adOptionsView = new AdOptionsView(act, bannerAd, adLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);
            LinearLayout ad_unit = linearLayout.findViewById(R.id.fb_ad_unit);
            TextView nativeAdTitle = linearLayout.findViewById(R.id.TxtFbNativeAdTitle);
            TextView nativeAdSocialContext = linearLayout.findViewById(R.id.TxtFbNativeAdSocialContext);
            TextView sponsoredLabel = linearLayout.findViewById(R.id.TxtFbNativeAdSponsoredLabel);
            com.facebook.ads.MediaView nativeAdIconView = linearLayout.findViewById(R.id.FbMediaNativeIconView);
            Button nativeAdCallToAction = linearLayout.findViewById(R.id.BtnFbNativeAdCallAction);
            nativeAdCallToAction.setSelected(true);
            nativeAdTitle.setSelected(true);
            nativeAdSocialContext.setSelected(true);
            sponsoredLabel.setSelected(true);
            ad_unit.setBackgroundColor(Color.parseColor(adsBackgroundcolor));
            nativeAdTitle.setTextColor(Color.parseColor(adsButtonTextColor));
            nativeAdSocialContext.setTextColor(Color.parseColor(adsButtonTextColor));
            sponsoredLabel.setTextColor(Color.parseColor(adsButtonTextColor));
            nativeAdCallToAction.setTextColor(Color.parseColor(adsButtonTextColor));
            nativeAdCallToAction.setBackgroundColor(Color.parseColor(adsButtonColor));

            nativeAdCallToAction.setText(bannerAd.getAdCallToAction());
            nativeAdCallToAction.setVisibility(bannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdTitle.setText(bannerAd.getAdvertiserName());
            nativeAdSocialContext.setText(bannerAd.getAdSocialContext());
            sponsoredLabel.setText(bannerAd.getSponsoredTranslation());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);
            bannerAd.registerViewForInteraction(linearLayout, nativeAdIconView, clickableViews);
        }
    }

    public static void ShowLayoutNativeBannerAds(Context con, ProgressBar bar, RelativeLayout layout) {
        if (IsInternetOn(con)) {
            bar.setVisibility(View.VISIBLE);
            BankConstantsData.LoadAdsData(con, new BankConstantsData.LoadAdsId() {
                @Override
                public void getAdsIds(ShowAdsModel showAdsModel) {
                    bar.setVisibility(View.GONE);
                    if (showAdsModel.getBtype().equalsIgnoreCase("1")) {
                        if (!showAdsModel.getNad().equalsIgnoreCase("")) {
                            showGAdMobNativeBanner((Activity) con, showAdsModel.getNad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getFnad());
                        } else {
                            Initialize(con);
                            showFbookNativeBanner((Activity) con, ((NativeAdLayout) ((Activity) con).findViewById(R.id.NativeBAdContainer)), showAdsModel.getFnbad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getNad());
                        }
                    } else {
                        if (!showAdsModel.getFnbad().equalsIgnoreCase("")) {
                            Initialize(con);
                            showFbookNativeBanner((Activity) con, ((NativeAdLayout) ((Activity) con).findViewById(R.id.NativeBAdContainer)), showAdsModel.getFnbad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getNad());
                        } else {
                            showGAdMobNativeBanner((Activity) con, showAdsModel.getNad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getFnad());
                        }
                    }
                }
            });
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    public static void showGAdMobNative(Activity act, String Gid, RelativeLayout layout, String shows, String adsButtonTextColor, String adsButtonColor, String adsbackgroundcolor, String fbooknad) {
        if (!Gid.equalsIgnoreCase("")) {
            AdLoader loader = new AdLoader.Builder(act, Gid)
                    .forNativeAd(nativeAd -> {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (nativeAd != null) {
                                    NativeAdView adView = (NativeAdView) act.getLayoutInflater().inflate(R.layout.layout_google_mob_native, (ViewGroup) null);
                                    GNativeAdView(nativeAd, adView, adsButtonColor, adsButtonTextColor, adsbackgroundcolor);
                                    layout.removeAllViews();

                                    if (shows.equalsIgnoreCase("t")) {
                                        layout.setVisibility(View.VISIBLE);
                                        layout.addView(adView);
                                    } else {
                                        layout.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }
                        }, 1000);
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            showFbookNative((Activity) act, ((NativeAdLayout) ((Activity) act).findViewById(R.id.NativeBAdContainer)), fbooknad, layout, shows, adsButtonTextColor, adsButtonColor, adsbackgroundcolor, Gid);
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder().build())
                    .build();
            loader.loadAd(new AdManagerAdRequest.Builder().build());
        }
    }

    public static void showFbookNative(Activity act, NativeAdLayout layout, String Fbid, RelativeLayout RlNativeAd, String shows, String adsButtonTextColor, String adsButtonColor, String adsBackgroundcolor, String Fbnad) {
        if (!Fbid.equalsIgnoreCase("")) {
            com.facebook.ads.NativeAd ad = new com.facebook.ads.NativeAd(act, Fbid);
            NativeAdListener nativeAdListener = new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {
                }

                @Override
                public void onError(Ad ad, com.facebook.ads.AdError adError) {
                    showGAdMobNative((Activity) act, Fbnad, RlNativeAd, shows, adsButtonTextColor, adsButtonColor, adsBackgroundcolor, Fbid);
                }

                @Override
                public void onAdLoaded(Ad ads) {
                    if (ads == null || ads != ads) {
                        return;
                    }
                    FNativeAd(act, layout, ad, adsButtonTextColor, adsButtonColor, adsBackgroundcolor);
                    RlNativeAd.removeAllViews();
                    if (shows.equalsIgnoreCase("t")) {
                        RlNativeAd.setVisibility(View.VISIBLE);
                        RlNativeAd.addView(layout);
                    } else {
                        RlNativeAd.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            ad.loadAd(ad.buildLoadAdConfig()
                    .withAdListener(nativeAdListener)
                    .build());
        }
    }

    private static void FNativeAd(Activity act, NativeAdLayout layout, com.facebook.ads.NativeAd ad, String adsButtonTextColor, String adsButtonColor, String adsBackgroundcolor) {
        ad.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(act);
        LinearLayout inflate = (LinearLayout) inflater.inflate(R.layout.layout_facebook_native_ad, layout, false);
        inflate.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, 850));
        layout.addView(inflate, new RelativeLayout.LayoutParams(MATCH_PARENT, 850));

        LinearLayout adChoicesContainer = inflate.findViewById(R.id.RlFbAdChoicesContainer);
        AdOptionsView adOptionsView = new AdOptionsView(act, ad, layout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        LinearLayout ad_unit = inflate.findViewById(R.id.fb_ad_unit);
        TextView nativeAdTitle = inflate.findViewById(R.id.TxtFbNativeAdTitle);
        TextView nativeAdSocialContext = inflate.findViewById(R.id.TxtFbNativeAdSocialContext);
        TextView sponsoredLabel = inflate.findViewById(R.id.TxtFbNativeAdSponsoredLabel);
        TextView nativeAdBody = inflate.findViewById(R.id.TxtFbNativeAdBody);
        com.facebook.ads.MediaView nativeAdIconView = inflate.findViewById(R.id.FbMediaNativeIconView);
        com.facebook.ads.MediaView MediaNativeAd = inflate.findViewById(R.id.FbMediaNativeAd);
        MediaNativeAd.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, 500));
        Button nativeAdCallToAction = inflate.findViewById(R.id.BtnFbNativeAdCallAction);
        nativeAdCallToAction.setSelected(true);
        nativeAdTitle.setSelected(true);
        nativeAdSocialContext.setSelected(true);
        sponsoredLabel.setSelected(true);
        ad_unit.setBackgroundColor(Color.parseColor(adsBackgroundcolor));
        nativeAdTitle.setTextColor(Color.parseColor(adsButtonTextColor));
        nativeAdSocialContext.setTextColor(Color.parseColor(adsButtonTextColor));
        sponsoredLabel.setTextColor(Color.parseColor(adsButtonTextColor));
        nativeAdCallToAction.setTextColor(Color.parseColor(adsButtonTextColor));
        nativeAdBody.setTextColor(Color.parseColor(adsButtonTextColor));
        nativeAdCallToAction.setBackgroundColor(Color.parseColor(adsButtonColor));

        nativeAdCallToAction.setText(ad.getAdCallToAction());
        nativeAdBody.setText(ad.getAdBodyText());
        nativeAdCallToAction.setVisibility(ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(ad.getAdvertiserName());
        nativeAdSocialContext.setText(ad.getAdSocialContext());
        sponsoredLabel.setText(ad.getSponsoredTranslation());

        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        ad.registerViewForInteraction(inflate, MediaNativeAd, nativeAdIconView, clickableViews);
    }

    public static void ShowLayoutNativeAds(Context con, ProgressBar bar, RelativeLayout layout) {
        if (IsInternetOn(con)) {
            bar.setVisibility(View.VISIBLE);
            BankConstantsData.LoadAdsData(con, new BankConstantsData.LoadAdsId() {
                @Override
                public void getAdsIds(ShowAdsModel showAdsModel) {
                    bar.setVisibility(View.GONE);
                    if (showAdsModel.getBtype().equalsIgnoreCase("1")) {
                        if (!showAdsModel.getNad().equalsIgnoreCase("")) {
                            showGAdMobNative((Activity) con, showAdsModel.getNad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getFnad());
                        } else {
                            Initialize(con);
                            showFbookNative((Activity) con, ((NativeAdLayout) ((Activity) con).findViewById(R.id.NativeBAdContainer)), showAdsModel.getFnad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getNad());
                        }
                    } else {
                        if (showAdsModel.getFnad().equalsIgnoreCase("")) {
                            showGAdMobNative((Activity) con, showAdsModel.getNad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getFnad());
                        } else {
                            Initialize(con);
                            showFbookNative((Activity) con, ((NativeAdLayout) ((Activity) con).findViewById(R.id.NativeBAdContainer)), showAdsModel.getFnad(), layout, showAdsModel.getLogin(), showAdsModel.getAppAdsButtonTextColor(), showAdsModel.getAppAdsButtonColor(), showAdsModel.getBackgroundcolor(), showAdsModel.getNad());
                        }
                    }
                }
            });
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    public static void Initialize(Context context) {
        AudienceNetworkAds.InitListener InitListener = new AudienceNetworkAds.InitListener() {
            @Override
            public void onInitialized(AudienceNetworkAds.InitResult initResult) {
                if (!AudienceNetworkAds.isInitialized(context)) {
                    if (BuildConfig.DEBUG) {
                        AdSettings.setTestMode(true);
                    }


                }
            }
        };
        Runnable logcatMonitor = () -> {
            try {
                Process process = Runtime.getRuntime().exec("logcat -d");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("FacebookAds") && line.contains("test mode device hash")) {
                        String testDeviceHash = line.substring(line.indexOf("test mode device hash:") + "test mode device hash:".length()).trim();
                        Log.d("FacebookAds", "Test device hash: " + testDeviceHash);
                        AdSettings.addTestDevice(testDeviceHash);
//                            AdSettings.addTestDevice("0fc7d369-b31e-4337-a907-697421dd2ddb");

                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        new Thread(logcatMonitor).start();
        AudienceNetworkAds
                .buildInitSettings(context)
                .withInitListener(InitListener)
                .initialize();
    }

    @SuppressLint("WrongConstant")
    private static void GNativeAdView(NativeAd ad, NativeAdView view, String adsButtonColor, String adsButtonTextColor, String adsBackgroundcolor) {
        view.setMediaView((MediaView) view.findViewById(R.id.MediaGAdMedia));
        view.setHeadlineView(view.findViewById(R.id.TvGAdHeadline));
        view.setBodyView(view.findViewById(R.id.TvGAdBody));
        Button button = view.findViewById(R.id.BtnGAdCallToAction);
        view.setCallToActionView(button);
        view.setIconView(view.findViewById(R.id.IvGAdIcon));
        view.setPriceView(view.findViewById(R.id.TvGAdPrice));
        view.setStarRatingView(view.findViewById(R.id.RbGAdStars));
        view.setStoreView(view.findViewById(R.id.TvGAdStore));
        view.setAdvertiserView(view.findViewById(R.id.TvGAdAdvertiser));
        button.setBackgroundColor(Color.parseColor(adsButtonColor));
        button.setTextColor(Color.parseColor(adsButtonTextColor));
        ((CardView) view.findViewById(R.id.CardGooNative)).setCardBackgroundColor(Color.parseColor(adsBackgroundcolor));
        ((TextView) view.findViewById(R.id.TvGAdBody)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.findViewById(R.id.TvGAdHeadline)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.findViewById(R.id.TvGAdPrice)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.findViewById(R.id.TvGAdStore)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.findViewById(R.id.TvGAdAdvertiser)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.findViewById(R.id.TvGooAd)).setTextColor(Color.parseColor(adsButtonTextColor));
        ((TextView) view.getHeadlineView()).setText(ad.getHeadline());
        if (ad.getBody() == null) {
            view.getBodyView().setVisibility(4);
        } else {
            view.getBodyView().setVisibility(0);
            ((TextView) view.getBodyView()).setText(ad.getBody());
        }
        if (ad.getCallToAction() == null) {
            view.getCallToActionView().setVisibility(4);
        } else {
            view.getCallToActionView().setVisibility(0);
            ((Button) view.getCallToActionView()).setText(ad.getCallToAction());
        }
        if (ad.getIcon() == null) {
            view.getIconView().setVisibility(8);
        } else {
            ((ImageView) view.getIconView()).setImageDrawable(ad.getIcon().getDrawable());
            view.getIconView().setVisibility(0);
        }
        if (ad.getPrice() == null) {
            view.getPriceView().setVisibility(4);
        } else {
            view.getPriceView().setVisibility(0);
            ((TextView) view.getPriceView()).setText(ad.getPrice());
        }
        if (ad.getStore() == null) {
            view.getStoreView().setVisibility(4);
        } else {
            view.getStoreView().setVisibility(0);
            ((TextView) view.getStoreView()).setText(ad.getStore());
        }
        if (ad.getStarRating() == null) {
            view.getStarRatingView().setVisibility(4);
        } else {
            ((RatingBar) view.getStarRatingView()).setRating(ad.getStarRating().floatValue());
            view.getStarRatingView().setVisibility(0);
        }
        if (ad.getAdvertiser() == null) {
            view.getAdvertiserView().setVisibility(4);
        } else {
            ((TextView) view.getAdvertiserView()).setText(ad.getAdvertiser());
            view.getAdvertiserView().setVisibility(0);
        }
        view.setNativeAd(ad);
    }
}

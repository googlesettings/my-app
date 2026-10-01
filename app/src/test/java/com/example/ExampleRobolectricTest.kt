package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.remote.MarketingEngine
import com.example.model.AdPlatform
import com.example.model.ContentLength
import com.example.model.ContentType
import com.example.model.Language
import com.example.model.Tone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SellAI", appName)
  }

  @Test
  fun `marketing engine generates valid copy`() {
    val result = MarketingEngine.generateWriterCopy(
      productInfo = "Eco-friendly coffee tumbler",
      contentType = ContentType.INSTAGRAM_CAPTION,
      targetAudience = "Remote workers",
      tone = Tone.PERSUASIVE,
      language = Language.ENGLISH,
      length = ContentLength.MEDIUM,
      cta = "Grab yours today"
    )
    assertTrue(result.isNotBlank())
    assertTrue(result.contains("Eco-friendly coffee tumbler") || result.contains("Remote workers"))
  }

  @Test
  fun `ads generator creates 3 variations`() {
    val variations = MarketingEngine.generateAds(
      product = "Ergonomic keyboard",
      audience = "Engineers",
      goal = "Conversions",
      platform = AdPlatform.FACEBOOK_ADS,
      tone = Tone.PERSUASIVE
    )
    assertEquals(3, variations.size)
    assertTrue(variations.all { it.headline.isNotBlank() && it.primaryText.isNotBlank() && it.cta.isNotBlank() })
  }

  @Test
  fun `email generator produces subject and body`() {
    val email = MarketingEngine.generateEmail(
      product = "Cashmere sweater",
      audience = "VIP subscribers",
      goal = "Flash sale",
      tone = Tone.FRIENDLY
    )
    assertTrue(email.subject.isNotBlank())
    assertTrue(email.subjectAlt.isNotBlank())
    assertTrue(email.previewText.isNotBlank())
    assertTrue(email.body.isNotBlank())
    assertTrue(email.cta.isNotBlank())
  }

  @Test
  fun `hashtags generator categorizes clusters`() {
    val tags = MarketingEngine.generateHashtags(
      topic = "Coffee beans",
      niche = "Artisan roast"
    )
    assertTrue(tags.highReach.isNotEmpty())
    assertTrue(tags.mediumCompetition.isNotEmpty())
    assertTrue(tags.niche.isNotEmpty())
  }

  @Test
  fun `product description generator provides structured output`() {
    val desc = MarketingEngine.generateProductDescription(
      name = "Desk Lamp",
      category = "Home & Living",
      features = "Touch dimming, USB-C",
      benefits = "Reduces eye fatigue",
      price = "$49",
      audience = "Designers",
      tone = Tone.LUXURY,
      language = Language.ENGLISH
    )
    assertNotNull(desc)
    assertTrue(desc.shortDescription.isNotBlank())
    assertTrue(desc.fullDescription.isNotBlank())
    assertTrue(desc.keyBenefits.isNotEmpty())
    assertTrue(desc.features.isNotEmpty())
  }

  @Test
  fun `campaign generator produces complete 14-section workspace output`() {
    val campaign = MarketingEngine.generateFullCampaign(
      product = "LuxeGlow Skincare",
      description = "Organic peptide hydration cream",
      audience = "Women 25-45",
      goal = "Product Launch",
      offer = "20% off launch week",
      platforms = setOf("Instagram", "TikTok", "Facebook", "Google", "Email", "WhatsApp"),
      duration = "7 Days",
      tone = Tone.PERSUASIVE
    )

    // Verify all 14 requirements
    assertEquals("LuxeGlow Skincare", campaign.productName)
    assertTrue(campaign.strategy.isNotBlank()) // 1. Strategy
    assertTrue(campaign.customerProfile.isNotBlank()) // 2. Profile
    assertTrue(campaign.coreMarketingAngle.isNotBlank()) // 3. Angle
    assertTrue(campaign.mainOffer.isNotBlank()) // 4. Offer
    assertTrue(campaign.usp.isNotBlank()) // 5. USP
    assertEquals(3, campaign.hooks.size) // 6. 3 Hooks
    assertEquals(3, campaign.instagramPosts.size) // 7. 3 Instagram posts
    assertEquals(3, campaign.tiktokConcepts.size) // 8. 3 TikTok scripts
    assertEquals(3, campaign.metaAds.size) // 9. 3 Meta ads
    assertEquals(3, campaign.googleAds.size) // 10. 3 Google ads
    assertEquals(3, campaign.emails.size) // 11. 3 Emails
    assertTrue(campaign.whatsappMessage.isNotBlank()) // 12. WhatsApp
    assertTrue(campaign.hashtags.isNotBlank()) // 13. Hashtags
    assertEquals(7, campaign.contentCalendar.size) // 14. 7-Day calendar

    // Test export markdown contains critical sections
    val markdown = campaign.toExportMarkdown()
    assertTrue(markdown.contains("COMPLETE MARKETING CAMPAIGN"))
    assertTrue(markdown.contains("1. CAMPAIGN STRATEGY"))
    assertTrue(markdown.contains("14. 7-DAY CONTENT CALENDAR"))
  }
}

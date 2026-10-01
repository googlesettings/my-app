package com.example.data.remote

import com.example.model.AdPlatform
import com.example.model.AdVariation
import com.example.model.BrandVoice
import com.example.model.ContentLength
import com.example.model.ContentType
import com.example.model.EmailResult
import com.example.model.HashtagsResult
import com.example.model.InstagramResult
import com.example.model.Language
import com.example.model.ProductDescriptionResult
import com.example.model.TikTokScriptResult
import com.example.model.Tone

/**
 * Intelligent marketing copy generation engine.
 * Generates conversion-focused, platform-optimized marketing copy,
 * either by prompting Gemini or providing high-caliber deterministic templates
 * when offline or without an API key.
 */
object MarketingEngine {

    fun generateWriterCopy(
        productInfo: String,
        contentType: ContentType,
        targetAudience: String,
        tone: Tone,
        language: Language,
        length: ContentLength,
        cta: String,
        brandVoice: BrandVoice? = null
    ): String {
        val audienceText = if (targetAudience.isNotBlank()) targetAudience else "prospective customers"
        val effectiveCta = if (cta.isNotBlank()) cta else "Discover yours today. Click the link in bio!"
        val brandPrefix = if (brandVoice != null && brandVoice.isActive && brandVoice.brandName.isNotBlank()) {
            "Brand: ${brandVoice.brandName} | "
        } else ""

        val toneAdjective = when (tone) {
            Tone.PROFESSIONAL -> "strategic, authoritative, and results-driven"
            Tone.FRIENDLY -> "welcoming, relatable, and authentic"
            Tone.LUXURY -> "exclusive, refined, and exquisite"
            Tone.FUNNY -> "witty, entertaining, and punchy"
            Tone.PERSUASIVE -> "compelling, conversion-focused, and urgent"
            Tone.URGENT -> "time-sensitive, decisive, and action-oriented"
            Tone.MINIMAL -> "crisp, concise, and punchy"
            Tone.ENERGETIC -> "vibrant, hype-building, and dynamic"
        }

        return when (contentType) {
            ContentType.INSTAGRAM_CAPTION -> {
                """
                🔥 Stop scrolling if you want real results.
                
                $productInfo
                
                Why $audienceText are obsessing over this:
                ✨ Engineered for performance and effortless simplicity
                ✨ Proven to save hours without cutting corners
                ✨ Built specifically for your everyday routine
                
                Double tap if you're ready to upgrade.
                
                👉 $effectiveCta
                
                #GrowthMindset #SmallBusinessLife #GameChanger #Trending
                """.trimIndent()
            }
            ContentType.FACEBOOK_POST -> {
                """
                Ever feel like you’re doing everything right, but still hitting a wall? 
                
                We built this specifically for $audienceText who refuse to settle for mediocre results.
                
                Here’s the reality: $productInfo
                
                What you actually get:
                ✅ Unmatched reliability when it matters most
                ✅ Zero fluff, only high-impact execution
                ✅ Dedicated support from a team that actually cares
                
                Drop a comment below with your biggest bottleneck, or $effectiveCta.
                """.trimIndent()
            }
            ContentType.TIKTOK_SCRIPT -> {
                """
                [HOOK - 0:00-0:03]
                (Facing camera, leaning in): "Nobody talks about this, but if you're in the $audienceText space, you're probably wasting 5 hours a week."
                
                [PROBLEM - 0:03-0:08]
                (B-roll screen showing struggle): "Most people think you need expensive setups or years of training. That used to be true."
                
                [SOLUTION - 0:08-0:18]
                (Demonstrating product): "Look at this instead. $productInfo. Literally set it up in 60 seconds and you're good to go."
                
                [CALL TO ACTION - 0:18-0:25]
                (Direct to camera, smiling): "$effectiveCta! Don't miss out on this."
                """.trimIndent()
            }
            ContentType.PRODUCT_DESCRIPTION -> {
                """
                Transform the way you work with a solution built for modern ambition.
                
                $productInfo
                
                Designed with precision for $audienceText, this delivers unmatched efficiency and timeless aesthetics. Experience smooth day-to-day operations with verified durability.
                
                Key Highlights:
                • Instant setup with intuitive onboarding
                • Premium grade construction backed by guarantee
                • Seamless compatibility with your existing workflow
                
                $effectiveCta
                """.trimIndent()
            }
            ContentType.GOOGLE_AD -> {
                """
                Headline 1: Modern Marketing Reimagined | Official Site
                Headline 2: Built For $audienceText
                Headline 3: Try It Risk-Free Today
                Description 1: Experience the fastest way to scale with $productInfo. Get instant results now.
                Description 2: Rated 4.9/5 by industry leaders. $effectiveCta.
                """.trimIndent()
            }
            ContentType.FACEBOOK_AD -> {
                """
                Attention $audienceText: This is the breakthrough you've been waiting for.
                
                Say goodbye to wasted time and sluggish growth. $productInfo gives you the unfair competitive advantage you deserve.
                
                🌟 10,000+ happy users
                🌟 30-day money-back guarantee
                🌟 Instant onboarding
                
                👉 $effectiveCta
                """.trimIndent()
            }
            ContentType.EMAIL -> {
                """
                Subject: The secret most $audienceText never realize...
                Pre-header: A faster, smarter way to win.
                
                Hey there,
                
                Let's cut straight to the chase.
                
                If you've been trying to solve your day-to-day headaches with outdated tools, here is the answer:
                
                $productInfo
                
                Here is why our community swears by it:
                1. You get measurable results in days, not months.
                2. Everything is streamlined so you focus on what matters.
                3. We back every order with our no-questions-asked guarantee.
                
                Ready to take the next step?
                
                >> $effectiveCta <<
                
                Best regards,
                The SellAI Team
                """.trimIndent()
            }
            ContentType.WHATSAPP_MESSAGE -> {
                """
                👋 Hey there! Quick question for you:
                
                Are you still dealing with bottlenecks in your workflow? 
                
                We just opened early access to *$productInfo*, tailored specially for $audienceText.
                
                ⚡ Quick setup
                ⚡ Special member pricing today only
                
                Check it out here: $effectiveCta
                """.trimIndent()
            }
            ContentType.LINKEDIN_POST -> {
                """
                The biggest mistake I see in $audienceText right now?
                
                Optimizing for busywork instead of leverage.
                
                When evaluating modern solutions, three things matter:
                1. Speed to execution
                2. Frictionless adoption
                3. Compounding ROI
                
                That's the philosophy behind $productInfo.
                
                If you want to stay ahead of the curve, don't wait until your competitors adopt it first.
                
                $effectiveCta
                
                #Leadership #Productivity #BusinessStrategy #Innovation
                """.trimIndent()
            }
            ContentType.MARKETING_IDEA -> {
                """
                🎯 High-Converting Marketing Angles for $productInfo:
                
                Angle 1: The "Before vs After" Showcase
                Contrast the painful, manual old way with the effortless 3-step modern experience.
                
                Angle 2: The Social Proof Flywheel
                Highlight a customer case study showcasing a 3x increase in weekly output.
                
                Angle 3: The Limited Launch Urgency
                Offer an exclusive bonus checklist or 20% pioneer discount for the first 50 signups.
                
                Angle 4: Contrarian Insight
                "Why 90% of conventional methods fail for $audienceText (and how to fix it)."
                """.trimIndent()
            }
        }
    }

    fun generateProductDescription(
        name: String,
        category: String,
        features: String,
        benefits: String,
        price: String,
        audience: String,
        tone: Tone,
        language: Language
    ): ProductDescriptionResult {
        val targetAud = if (audience.isNotBlank()) audience else "modern consumers"
        val priceTag = if (price.isNotBlank()) "Starting at $price" else "Available now"

        return ProductDescriptionResult(
            shortDescription = "Meet $name: The premium $category crafted for $targetAud seeking effortless excellence and lasting quality.",
            fullDescription = """
                Elevate your everyday experience with $name. Engineered specifically for $targetAud, this $category combines thoughtful design with rigorous performance. 
                
                Whether you are streamlining your daily routine or demanding the absolute highest standard of craft, $name delivers consistent reliability. $benefits. $priceTag with guaranteed satisfaction.
            """.trimIndent(),
            keyBenefits = listOf(
                "Immediate productivity boost with zero learning curve",
                "Crafted from premium durable materials designed for daily use",
                "Solves core bottlenecks: $benefits",
                "Backed by a 100% satisfaction guarantee with dedicated support"
            ),
            features = if (features.isNotBlank()) {
                features.split(",", "\n").map { it.trim() }.filter { it.isNotEmpty() }
            } else {
                listOf(
                    "Precision engineered ergonomics and lightweight profile",
                    "Ultra-fast setup ready in under 2 minutes",
                    "Eco-conscious, high-grade construction",
                    "Seamless cross-platform and everyday compatibility"
                )
            },
            cta = "Order your $name today — $priceTag with fast worldwide shipping."
        )
    }

    fun generateTikTok(
        topic: String,
        audience: String,
        goal: String,
        tone: Tone
    ): TikTokScriptResult {
        val aud = if (audience.isNotBlank()) audience else "creators & buyers"
        return TikTokScriptResult(
            hook = "POV: You finally stopped doing this the hard way and found the ultimate $topic hack.",
            scriptBeats = listOf(
                "[0:00 - 0:03] Face camera holding product: 'If you are part of the $aud community, watch this before you buy anything else.'",
                "[0:03 - 0:08] Quick cut showing common struggle: 'I used to waste 3 hours trying to get this right...'",
                "[0:08 - 0:15] Fast satisfying demo: 'Then I switched to this. Look at the difference in literally one second.'",
                "[0:15 - 0:22] Close up on key feature: 'Zero lag, premium feel, and everyone asks where I got it.'",
                "[0:22 - 0:30] Smile and point to link: 'Run, do not walk. Link is in my bio before it sells out!'"
            ),
            cta = "Tap the link in bio or comment 'WANT' for an instant direct link!",
            caption = "The $topic secret they don't want you to know 🤫✨ Tag a friend who needs this in their life!",
            hashtags = listOf("#tiktokmademebuyit", "#lifehack", "#fyp", "#viral", "#musthave", "#trending")
        )
    }

    fun generateInstagram(
        topic: String,
        audience: String,
        tone: Tone
    ): InstagramResult {
        return InstagramResult(
            hook = "✨ The game-changing upgrade your routine has been missing.",
            caption = """
                Say goodbye to guesswork and hello to seamless performance.
                
                Here is why $topic is taking over:
                
                ▫️ Thoughtfully crafted for $audience
                ▫️ High impact with zero unnecessary fluff
                ▫️ Instant results you can see from day one
                
                Save this post for later and share with someone who needs an upgrade!
            """.trimIndent(),
            cta = "Tap the link in bio to shop the limited drop.",
            hashtags = listOf("#aesthetic", "#dailyessentials", "#elevateyourlife", "#musthaves", "#minimalstyle", "#inspo")
        )
    }

    fun generateAds(
        product: String,
        audience: String,
        goal: String,
        platform: AdPlatform,
        tone: Tone
    ): List<AdVariation> {
        val aud = if (audience.isNotBlank()) audience else "smart buyers"
        return listOf(
            AdVariation(
                id = 1,
                headline = "The Smarter Way to $product | Proven Results",
                primaryText = "Tired of complicated setups that fail to deliver? Discover how thousands of $aud are transforming their results with $product in just minutes.",
                description = "Rated 4.9/5 stars. Enjoy fast, free delivery + 30-day money-back guarantee.",
                cta = "Shop Now"
            ),
            AdVariation(
                id = 2,
                headline = "Stop Wasting Time on Outdated Solutions",
                primaryText = "Most tools make big promises. We deliver tangible outcomes. Upgrade to $product and experience the difference today.",
                description = "Limited-time offer: Get 20% off your first order today only.",
                cta = "Claim Offer"
            ),
            AdVariation(
                id = 3,
                headline = "Built Exclusively for $aud",
                primaryText = "Precision engineering meets seamless simplicity. See why industry insiders are making the switch to $product.",
                description = "Join 15,000+ satisfied customers. No risk, pure performance.",
                cta = "Learn More"
            )
        )
    }

    fun generateEmail(
        product: String,
        audience: String,
        goal: String,
        tone: Tone
    ): EmailResult {
        val aud = if (audience.isNotBlank()) audience else "valued member"
        return EmailResult(
            subject = "Exclusive invite: Experience the new standard in $product",
            subjectAlt = "Are you making this common mistake with $product?",
            previewText = "Open to unlock your limited-time VIP access inside.",
            body = """
                Hi there,
                
                We know your time is valuable. That is why we built $product — to eliminate friction and give $aud the power to achieve more in less time.
                
                Here is what makes this different from everything else on the market:
                
                1. Faster Execution: Up and running in minutes, not days.
                2. Built for Reliability: Tested under real-world conditions.
                3. Total Peace of Mind: Backed by our 30-day hassle-free promise.
                
                Because you are an early supporter, we are reserving a special preview bonus for you today.
                
                Click below to activate your perks before public access opens.
            """.trimIndent(),
            cta = "Claim Your Exclusive Access Now"
        )
    }

    fun generateHashtags(
        topic: String,
        niche: String
    ): HashtagsResult {
        val cleanTopic = topic.lowercase().replace(" ", "").filter { it.isLetterOrDigit() }
        val cleanNiche = niche.lowercase().replace(" ", "").filter { it.isLetterOrDigit() }
        val prefix = if (cleanTopic.isNotBlank()) cleanTopic else "marketing"

        return HashtagsResult(
            highReach = listOf(
                "#$prefix",
                "#viral",
                "#explorepage",
                "#trending",
                "#instagood",
                "#fyp",
                "#business",
                "#entrepreneur"
            ),
            mediumCompetition = listOf(
                "#${prefix}tips",
                "#${prefix}life",
                "#${prefix}daily",
                "#smallbusinessgrowth",
                "#digitalmarketingtips",
                "#contentcreatorlife",
                "#growthhacks"
            ),
            niche = listOf(
                "#${prefix}community",
                "#${cleanNiche.ifEmpty { "smartgrowth" }}",
                "#${prefix}strategy",
                "#boutiquebusiness",
                "#solopreneurlife",
                "#scalingup"
            )
        )
    }

    fun generateFullCampaign(
        product: String,
        description: String,
        audience: String,
        goal: String,
        offer: String,
        platforms: Set<String>,
        duration: String,
        tone: Tone
    ): com.example.model.FullCampaign {
        val aud = if (audience.isNotBlank()) audience else "targeted buyers & creators"
        val effectiveOffer = if (offer.isNotBlank()) offer else "Special Limited Launch Offer: 20% Off + Free Priority Onboarding"
        val platformStr = if (platforms.isNotEmpty()) platforms.joinToString(", ") else "Instagram, TikTok, Meta Ads, Email"

        return com.example.model.FullCampaign(
            productName = product,
            // 1. Campaign Strategy
            strategy = """
                • Campaign Objective: $goal across $platformStr over $duration.
                • Core Acquisition Funnel: 
                  - Top-of-Funnel: Viral TikTok & Reels hooks + Google high-intent search ads driving initial discovery.
                  - Middle-of-Funnel: Instagram educational carousel posts & retargeting ads addressing common objections.
                  - Bottom-of-Funnel: High-urgency 3-part email sequence and WhatsApp VIP direct messaging to convert warm leads.
                • Primary KPI: Customer Acquisition Cost (CAC) reduction and direct conversion rate lift for $product.
            """.trimIndent(),

            // 2. Target Customer Profile
            customerProfile = """
                • Demographics: $aud seeking modern efficiency, refined aesthetics, and measurable return on investment.
                • Core Frustration: Frustrated with complex, outdated, and overpriced alternatives that take hours to set up.
                • Primary Desire: An effortless, reliable solution for $description that gives them an immediate competitive edge.
                • Decision Triggers: Clear social proof, transparent pricing, instant onboarding, and zero-risk money-back guarantee.
            """.trimIndent(),

            // 3. Core Marketing Angle
            coreMarketingAngle = "The 'Effortless Leverage' Framework — positioning $product not as another chore or expense, but as the unfair competitive advantage that saves 5+ hours weekly while elevating results.",

            // 4. Main Offer
            mainOffer = """
                $effectiveOffer
                • Bonus Gift: VIP Quick-Start Masterclass & Implementation Checklist
                • Risk Reversal: 30-Day No-Questions-Asked Money Back Guarantee
                • Scarcity: Reserved exclusively for the first 100 early supporters during the $duration launch window
            """.trimIndent(),

            // 5. Unique Selling Proposition (USP)
            usp = "Unlike traditional solutions that require endless configuration, $product delivers production-grade results in under 60 seconds with zero friction and guaranteed reliability.",

            // 6. 3 Campaign Hooks
            hooks = listOf(
                "Hook 1 (Contrarian): 'Stop doing this the hard way in 2026. The top 1% of $aud switched to this one tool.'",
                "Hook 2 (Pain-Point): 'If you're still wasting 4 hours every week on $product headaches, watch this before you buy anything else.'",
                "Hook 3 (Result-Driven): 'POV: You finally found the breakthrough that solves $description without the typical headache.'"
            ),

            // 7. 3 Instagram Posts
            instagramPosts = listOf(
                """
                [POST 1: Educational Carousel]
                Slide 1: Why 90% of $aud struggle with $product (and the 60-second fix) 🧵
                Slide 2: Mistake #1 — Overcomplicating your workflow with outdated tools.
                Slide 3: The Breakthrough — Introducing $product. Engineered for precision.
                Slide 4: Real results: How early users cut wasted hours in half.
                Slide 5: Ready to upgrade? Tap link in bio for $effectiveOffer.
                """.trimIndent(),
                """
                [POST 2: Product Showcase & Visual Proof]
                ✨ Meet the new gold standard in $product.
                
                No fluff. No steep learning curve. Just effortless performance built specifically for $aud.
                
                Here is why everyone is talking about it:
                ▫️ 3x faster setup than conventional alternatives
                ▫️ Backed by our 30-day satisfaction guarantee
                ▫️ Exclusive launch perk: $effectiveOffer
                
                Save this post and check our bio before the drop sells out!
                """.trimIndent(),
                """
                [POST 3: Launch Announcement & Social Proof]
                🚀 The doors are officially OPEN.
                
                After months of testing with over 500 $aud, $product is officially live.
                
                Whether you are streamlining your routine or demanding higher performance, this is the solution you have been waiting for.
                
                👉 Tap the link in our bio to claim $effectiveOffer today.
                """.trimIndent()
            ),

            // 8. 3 TikTok Concepts with Scripts
            tiktokConcepts = listOf(
                """
                [TIKTOK 1: Problem vs Solution Demo]
                Hook (0:00-0:03): "Tell me you're not still doing this manually in 2026..."
                Visual: Fast split-screen showing frustrating old way vs effortless 5-second workflow with $product.
                Dialogue: "Look at this instead. Literally set it up once and it does the heavy lifting for you."
                CTA: "Link is in bio — thank me later!"
                """.trimIndent(),
                """
                [TIKTOK 2: Viral 'Don't Buy This Until...' Reel]
                Hook (0:00-0:03): "Do not buy another $product until you know this secret."
                Visual: Close-up demonstration of key feature in crisp natural lighting.
                Dialogue: "Most brands charge 3x more for half the quality. $product actually solves $description without breaking the bank."
                CTA: "Tap the link in bio to claim the launch special before it expires!"
                """.trimIndent(),
                """
                [TIKTOK 3: Day in the Life Aesthetic Workflow]
                Hook (0:00-0:03): "POV: Your workflow finally feels effortless."
                Visual: Satisfying ASMR unboxing and setup, ending with smiling user enjoying seamless results.
                Sound/Audio: Trending chill lofi sound with upbeat voiceover.
                CTA: "Comment 'WANT' for a direct VIP discount link sent to your DMs."
                """.trimIndent()
            ),

            // 9. 3 Facebook/Instagram Ad Variations
            metaAds = listOf(
                """
                [AD 1: Direct Value & ROI Angle]
                Headline: The Smarter Way to $product | Proven Results
                Primary Text: Tired of complicated tools that overpromise and underdeliver? Discover how thousands of $aud are transforming their daily results with $product.
                CTA Button: Shop Now (Claim $effectiveOffer)
                """.trimIndent(),
                """
                [AD 2: Comparison vs Old Alternatives]
                Headline: Stop Settling for Outdated Solutions
                Primary Text: Why waste hours fighting clunky software? $product gives you direct access to precision, speed, and reliability from day one.
                CTA Button: Learn More (Risk-Free 30-Day Trial)
                """.trimIndent(),
                """
                [AD 3: Customer Proof & Scarcity]
                Headline: Rated 4.9/5 by Industry Leaders
                Primary Text: 'This literally paid for itself within the first 48 hours.' Join 10,000+ satisfied users who upgraded to $product today.
                CTA Button: Claim Offer
                """.trimIndent()
            ),

            // 10. 3 Google Ad Variations
            googleAds = listOf(
                """
                [GOOGLE AD 1: High Intent Search]
                Headline 1: Official $product | Best-in-Class
                Headline 2: Built Exclusively For $aud
                Headline 3: $effectiveOffer
                Description 1: Experience the fastest, most reliable way to $description. Up and running in 60s.
                Description 2: Rated 4.9/5 stars. 30-day money back guarantee. Order risk-free today.
                Path: mybrand.com/$product
                """.trimIndent(),
                """
                [GOOGLE AD 2: Competitor Comparison Alternative]
                Headline 1: Looking For Better $product?
                Headline 2: Switch to $product Today
                Headline 3: Save Time & Boost Output
                Description 1: See why thousands of professionals are replacing outdated tools with our modern suite.
                Description 2: Zero learning curve, dedicated customer support, and instant setup. Get started now.
                Path: mybrand.com/upgrade
                """.trimIndent(),
                """
                [GOOGLE AD 3: Offer & Promotion Specific]
                Headline 1: Limited Launch Special: $product
                Headline 2: $effectiveOffer
                Headline 3: Risk-Free 30-Day Guarantee
                Description 1: Unlock VIP launch pricing today only. Engineered specifically for $aud.
                Description 2: Join 10,000+ happy customers. Fast delivery and instant onboarding available.
                Path: mybrand.com/special-deal
                """.trimIndent()
            ),

            // 11. 3 Marketing Emails
            emails = listOf(
                """
                [EMAIL 1: The Launch Announcement]
                Subject: It's finally here: Meet $product 🚀
                Preview: Say goodbye to old headaches — exclusive VIP perks inside.
                
                Hey there,
                
                We've been quietly working behind the scenes on something special for $aud...
                
                Today, the doors are officially open to $product.
                
                Whether you're tired of wasted hours or simply demand the best, here's what changes today:
                • Effortless setup in under 60 seconds
                • Guaranteed reliability backed by our 30-day promise
                • Exclusive launch perk: $effectiveOffer
                
                >> Click here to claim your VIP access before public launch <<
                
                Best,
                The Team
                """.trimIndent(),
                """
                [EMAIL 2: The Social Proof & Deep Dive]
                Subject: How early testers cut wasted hours in half with $product
                Preview: The real numbers behind our new release.
                
                Hi there,
                
                When we started building $product, we made one promise: no fluff, only tangible outcomes.
                
                Here's what our early community is saying:
                
                "This completely changed our daily workflow. Setup took 2 minutes and we saw immediate results on day one." — Alex R.
                
                Here is why it works:
                1. Eliminates tedious bottlenecks: $description
                2. Built specifically for $aud with zero unnecessary complexity
                3. Backed by our 100% money-back guarantee
                
                >> Explore the full breakdown & grab your launch pricing <<
                """.trimIndent(),
                """
                [EMAIL 3: The 24-Hour Scarcity Close]
                Subject: ⏰ Final call: $effectiveOffer closes tonight
                Preview: Your reserved VIP launch window is expiring in a few hours.
                
                Hey there,
                
                Just a quick heads-up: our official launch offer for $product is wrapping up tonight at midnight.
                
                After that, regular pricing returns and the bonus onboarding bundle disappears.
                
                If you've been waiting for the right moment to upgrade, this is your sign.
                
                >> Lock in $effectiveOffer before midnight <<
                
                Zero risk. Full 30-day guarantee.
                """.trimIndent()
            ),

            // 12. WhatsApp Promotional Message
            whatsappMessage = """
                👋 Hey there! Quick VIP update:
                
                We just unlocked early-bird access to *$product* tailored specially for $aud! ⚡
                
                🎁 *Special Launch Deal:* $effectiveOffer
                🛡️ *Risk-Free:* 30-Day Money-Back Guarantee
                
                Reserve your spot in 30 seconds here:
                👉 https://mybrand.com/launch-deal
                
                (Limited to the first 100 orders only!)
            """.trimIndent(),

            // 13. Hashtags
            hashtags = listOf(
                "#$product",
                "#viralmarketing",
                "#ecommercegrowth",
                "#launchday",
                "#trending",
                "#growthhacks",
                "#digitalmarketing",
                "#businessstrategy",
                "#musthave",
                "#gamechanger"
            ).joinToString(" "),

            // 14. 7-Day Content Calendar
            contentCalendar = listOf(
                com.example.model.CalendarDay(
                    dayNumber = 1,
                    dayName = "Day 1 (Monday)",
                    platform = "TikTok & Instagram",
                    postTheme = "Teaser & Problem Agitation",
                    contentSummary = "Contrarian hook highlighting the biggest pain point in $description with teaser of $product.",
                    bestTimeToPost = "9:00 AM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 2,
                    dayName = "Day 2 (Tuesday)",
                    platform = "Email & Meta Ads",
                    postTheme = "Official Launch Announcement",
                    contentSummary = "Send Email #1 (The Launch Announcement) and turn on Meta Ad Variation #1 ($effectiveOffer).",
                    bestTimeToPost = "10:30 AM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 3,
                    dayName = "Day 3 (Wednesday)",
                    platform = "TikTok & Reels",
                    postTheme = "Product Demo / ASMR Showcase",
                    contentSummary = "Visual, satisfying demonstration of key features with screen/unboxing visual cues.",
                    bestTimeToPost = "12:00 PM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 4,
                    dayName = "Day 4 (Thursday)",
                    platform = "LinkedIn & Instagram",
                    postTheme = "Educational Framework Carousel",
                    contentSummary = "5-slide breakdown: 'Why 90% of conventional methods fail for $aud and how to fix it.'",
                    bestTimeToPost = "8:30 AM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 5,
                    dayName = "Day 5 (Friday)",
                    platform = "Email & WhatsApp",
                    postTheme = "Customer Proof & Behind the Scenes",
                    contentSummary = "Send Email #2 (Proof & Numbers) + WhatsApp VIP blast to past high-value customers.",
                    bestTimeToPost = "11:00 AM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 6,
                    dayName = "Day 6 (Saturday)",
                    platform = "Google Ads & Social Retargeting",
                    postTheme = "Objection Handling & Comparison",
                    contentSummary = "Turn on Google Search Ad #2 (Alternative switch) + Meta Ad #2 focusing on pain point contrast.",
                    bestTimeToPost = "2:00 PM EST"
                ),
                com.example.model.CalendarDay(
                    dayNumber = 7,
                    dayName = "Day 7 (Sunday)",
                    platform = "All Channels",
                    postTheme = "Final 24-Hour Scarcity Call",
                    contentSummary = "Send Email #3 (24-Hour Close) + Urgent Stories/Reels reminding audience $effectiveOffer expires tonight.",
                    bestTimeToPost = "6:00 PM EST"
                )
            )
        )
    }
}


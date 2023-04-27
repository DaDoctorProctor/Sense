package com.example.homerosense.presentation.screen.monitor


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.ui.theme.AppleGray

/* Core functionality of the history feature*/
private val dataSet1 : MutableList<String> = arrayListOf()
private var characteristicDefault1 = -1
private val values1 = mutableListOf(Point(0f, 0f))

@Composable
fun Ideas2(viewModel: TempHumidityViewModel) {
    Column() {
        //TabContentScreen(data = "Hello", navController = navController)
        Update1(viewModel = viewModel)
    }


}


@Composable
fun Update1(viewModel: TempHumidityViewModel){
    val temperatureString = viewModel.temperaturehumidityDate
    dataSet1.add("$temperatureString")
    //dataSet.add("$characteristicDefault")
    DisplayList1(dataSet1)
    characteristicDefault1 += 1
}

private var pad = 15;

@Composable
fun DisplayList1(languages: MutableList<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppleGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(15.dp))
//                .fillMaxHeight()
//                .padding(10.dp)
//                .background(Color.White, RoundedCornerShape(15.dp)),
//        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            ) {

                val langx = languages.takeLast(76)
                for (language in langx) {
                    Text(language, modifier = Modifier.padding(pad.dp))
                    Divider()
                }

            }

//        }

    }
}





//fun Modifier.simpleVerticalScrollbar(
//    state: LazyListState,
//    width: Dp = 8.dp
//): Modifier = composed {
//    val targetAlpha = if (state.isScrollInProgress) 1f else 0f
//    val duration = if (state.isScrollInProgress) 150 else 500
//
//    val alpha by animateFloatAsState(
//        targetValue = targetAlpha,
//        animationSpec = tween(durationMillis = duration)
//    )
//
//    drawWithContent {
//        drawContent()
//
//        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
//        val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f
//
//        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
//        if (needDrawScrollbar && firstVisibleElementIndex != null) {
//            val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
//            val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
//            val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight
//
//            drawRect(
//                color = Color.Red,
//                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
//                size = Size(width.toPx(), scrollbarHeight),
//                alpha = alpha
//            )
//        }
//    }
//}


